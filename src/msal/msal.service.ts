// eslint-disable-next-line node/no-unpublished-import
import { User } from "@microsoft/microsoft-graph-types-beta";
import { Injectable } from "@nestjs/common";

import { apiConfig, msalApiQuery } from "../config/msal.config";
import { PrismaService } from "../prisma.service";

@Injectable()
export class MsalService {
  constructor(private prisma: PrismaService) {}

  async getUsers(): Promise<void> {
    const users = await msalApiQuery<User[]>(apiConfig.users, {
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
}
