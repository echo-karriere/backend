// eslint-disable-next-line node/no-unpublished-import
import { User } from "@microsoft/microsoft-graph-types-beta";
import { Injectable } from "@nestjs/common";

import { apiConfig, getToken, msalApiQuery, tokenRequest } from "../config/msal.config";
import { PrismaService } from "../prisma.service";

@Injectable()
export class MsalService {
  constructor(private prisma: PrismaService) {}

  async getUsers(): Promise<void> {
    const token = await getToken(tokenRequest);
    const users = await msalApiQuery<{ value: User[] }>(apiConfig.users, token.accessToken);

    if (users instanceof Error) return;

    for (const user of users.value) {
      await this.prisma.user.upsert({
        where: { id: user.id },
        create: {
          id: user.id,
        },
        update: {},
      });
    }
  }
}
