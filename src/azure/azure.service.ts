// eslint-disable-next-line node/no-unpublished-import
import { Group, User } from "@microsoft/microsoft-graph-types-beta";
import { BadRequestException, Injectable, OnApplicationBootstrap } from "@nestjs/common";
import generator from "generate-password";

import { PrismaService } from "../prisma.service";
import { GraphApiResponse } from "./azure.config";
import { CreateUserData } from "./dto/create-user.data";
import { UpdateUserData } from "./dto/update-user.data";
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

  async updateUser(id: string, data: UpdateUserData): Promise<void> {
    const updatedInfo = Object.assign(
      {},
      data.name === undefined ? null : { displayName: data.name },
      data.email === undefined ? null : { mail: data.email },
      data.enabled === undefined ? null : { accountEnabled: data.enabled },
    );
    await this.graphService
      .api(`/users/${id}`)
      .update(updatedInfo)
      .catch((error) => {
        console.log(error);
        throw new BadRequestException("Could not update user.");
      });
  }

  async addMembersToGroup(group: string, ...members: string[]): Promise<void> {
    const data = {
      "members@odata.bind": members.map((id) => `https://graph.microsoft.com/v1.0/directoryObjects/${id}`),
    };

    await this.graphService
      .api(`/groups/${group}`)
      .update(data)
      .catch((error) => {
        console.log(error);
        throw new BadRequestException("Could not assign users to group.");
      });
  }

  async removeMembersFromGroup(group: string, ...members: string[]): Promise<void> {
    for (const id of members) {
      await this.graphService
        .api(`/groups/${group}/members/${id}/$ref`)
        .delete()
        .catch((error) => {
          console.log(error);
          throw new BadRequestException("Could not delete user from group.");
        });
    }
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
