import { Injectable } from "@nestjs/common";
import { Prisma, Role } from "@prisma/client";

import { PrismaService } from "../prisma.service";

@Injectable()
export class RoleService {
  constructor(private readonly prisma: PrismaService) {}
  findAll(where: Prisma.RoleWhereInput): Promise<Role[]> {
    return this.prisma.role.findMany({ where });
  }

  findOne(where: Prisma.RoleWhereUniqueInput): Promise<Role | null> {
    return this.prisma.role.findUnique({ where });
  }
}
