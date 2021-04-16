// eslint-disable-next-line node/no-unpublished-import
import { Group, User } from "@microsoft/microsoft-graph-types-beta";
import { Injectable } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { msalApiEndpoints } from "./azure.config";
import { GraphService } from "./graph.service";

@Injectable()
export class MsalService {
  constructor(private prisma: PrismaService, private graphService: GraphService) {}
  async getUsers(): Promise<void> {
    const users = await this.graphService.query<User[]>(msalApiEndpoints.users, {
      params: { $select: "id,accountEnabled,displayName" },
    });

    if (users instanceof Error) return;

    for (const user of users) {
      await this.prisma.user.upsert({
        where: { id: user.id },
        create: {
          id: user.id,
          enabled: user.accountEnabled,
          name: user.displayName,
        },
        update: {
          enabled: user.accountEnabled,
          name: user.displayName,
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
