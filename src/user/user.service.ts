import { Injectable } from "@nestjs/common";
import { Prisma, Role, User } from "@prisma/client";

import { PrismaService } from "../prisma.service";

@Injectable()
export class UserService {
  constructor(private readonly prisma: PrismaService) {}

  async findOne(where: Prisma.UserWhereUniqueInput): Promise<(User & { roles: Role[] }) | null> {
    return this.prisma.user.findUnique({
      where,
      include: {
        roles: true,
      },
    });
  }
}
