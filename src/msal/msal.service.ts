// eslint-disable-next-line node/no-unpublished-import
import { Group, User } from "@microsoft/microsoft-graph-types-beta";
import { Injectable } from "@nestjs/common";

import { msalApiEndpoints, msalApiQuery } from "../config/msal.config";
import { PrismaService } from "../prisma.service";

@Injectable()
export class MsalService {
  constructor(private prisma: PrismaService) {}

  async getUsers(): Promise<void> {
    const users = await msalApiQuery<User[]>(msalApiEndpoints.users, {
      $select: "id,accountEnabled,displayName",
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
    const groups = await msalApiQuery<Group[]>(msalApiEndpoints.groups);

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
}
