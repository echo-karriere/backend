// eslint-disable-next-line node/no-unpublished-import
import { Group, User } from "@microsoft/microsoft-graph-types-beta";
import { BadRequestException, Injectable, OnApplicationBootstrap } from "@nestjs/common";
import generator from "generate-password";

import { PrismaService } from "../prisma.service";
import { CreateUserInput } from "../user/dto/create-user.input";
import { msalApiEndpoints } from "./azure.config";
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

  async createUser(data: CreateUserInput): Promise<User> {
    const created = await this.graphService.query<User>(msalApiEndpoints.users, {
      method: "POST",
      body: {
        accountEnabled: true,
        displayName: data.name,
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
      },
    });

    if (created instanceof Error) throw new BadRequestException("Could not create user");
    return created;
  }

  async getUsers(): Promise<void> {
    const users = await this.graphService.query<User[]>(msalApiEndpoints.users, {
      params: { $select: "id,accountEnabled,displayName,mail,userPrincipalName" },
    });

    if (users instanceof Error) return;

    for (const user of users) {
      await this.prisma.user.upsert({
        where: { id: user.id },
        create: {
          id: user.id,
          enabled: user.accountEnabled,
          name: user.displayName,
          email: user.userPrincipalName ?? user.mail,
        },
        update: {
          enabled: user.accountEnabled,
          name: user.displayName,
          email: user.userPrincipalName ?? user.mail,
        },
      });
    }
  }

  async getRoles(): Promise<void> {
    const groups = await this.graphService.query<Group[]>(msalApiEndpoints.groups);

    if (groups instanceof Error) return;

    for (const group of groups) {
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
  }

  async assignRoles(): Promise<void> {
    const users = await this.prisma.user.findMany();

    for (const user of users) {
      const roles = await this.graphService.query<string[]>(msalApiEndpoints.userGroups(user.id), {
        body: { securityEnabledOnly: true },
        method: "POST",
      });

      if (roles instanceof Error) return;

      await this.prisma.user.update({
        where: { id: user.id },
        data: {
          roles: {
            connect: roles.map((id) => Object.assign({}, { id: id })),
          },
        },
      });
    }
  }
}
