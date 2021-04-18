// eslint-disable-next-line node/no-unpublished-import
import { Group, User } from "@microsoft/microsoft-graph-types-beta";
import { BadRequestException, Injectable, OnApplicationBootstrap } from "@nestjs/common";
import generator from "generate-password";

import { PrismaService } from "../prisma.service";
import { GraphApiResponse } from "./azure.config";
import { CreateUserData } from "./dto/create-user.data";
import { GraphService } from "./graph.service";

@Injectable()
export class AzureService implements OnApplicationBootstrap {
  constructor(private prisma: PrismaService, private graphService: GraphService) {}

  async onApplicationBootstrap(): Promise<void> {
    if (process.env.NODE_ENV === "production" || process.env.RELOAD_AZURE !== undefined) {
      await this.getUsers();
      await this.getRoles();
      await this.assignRoles();
    }

    return;
  }

  async createUser(data: CreateUserData): Promise<User> {
    return await this.graphService
      .api("/users")
      .create({
        accountEnabled: true,
        displayName: data.name,
        mail: data.email,
        identities: [
          {
            signInType: "emailAddress",
            issuer: `${process.env.AZURE_TENANT_NAME}.onmicrosoft.com`,
            issuerAssignedId: data.email,
          },
        ],
        passwordPolicies: "DisablePasswordExpiration",
        passwordProfile: {
          forceChangePasswordNextSignIn: true,
          password: generator.generate({ length: 16, symbols: true, numbers: true }),
        },
      })
      .then((user: User) => user)
      .catch((error) => {
        console.log(error);
        throw new BadRequestException("Could not create user.");
      });
  }

  async getUsers(): Promise<void> {
    return await this.graphService
      .api("/users")
      .select(["id", "accountEnabled", "displayName", "mail", "userPrincipalName"])
      .get()
      .then(async (users: GraphApiResponse<User[]>) => {
        for (const user of users.value) {
          await this.prisma.user.upsert({
            where: { id: user.id },
            create: {
              id: user.id,
              enabled: user.accountEnabled,
              name: user.displayName,
              email: user.mail ?? user.userPrincipalName,
            },
            update: {
              enabled: user.accountEnabled,
              name: user.displayName,
              email: user.mail ?? user.userPrincipalName,
            },
          });
        }
      })
      .catch((error) => {
        console.log(error);
        throw new BadRequestException("Could not fetch users");
      });
  }

  async getRoles(): Promise<void> {
    return await this.graphService
      .api("/groups")
      .get()
      .then(async (groups: GraphApiResponse<Group[]>) => {
        for (const group of groups.value) {
          await this.prisma.role.upsert({
            where: { id: group.id },
            create: {
              id: group.id,
              name: group.displayName,
              description: group.description,
            },
            update: {
              name: group.displayName,
              description: group.description,
            },
          });
        }
      })
      .catch((error) => {
        console.log(error);
        throw new BadRequestException("Could not fetch groups");
      });
  }

  async assignRoles(): Promise<void> {
    const users = await this.prisma.user.findMany();

    for (const user of users) {
      await this.graphService
        .api(`/users/${user.id}/getMemberGroups`)
        .post({ securityEnabledOnly: true })
        .then(async (roles: GraphApiResponse<string[]>) => {
          await this.prisma.user.update({
            where: { id: user.id },
            data: {
              roles: {
                connect: roles.value.map((id) => {
                  return { id: id };
                }),
              },
            },
          });
        })
        .catch((error) => {
          console.log(error);
          throw new BadRequestException("Could not assign roles");
        });
    }
  }
}
