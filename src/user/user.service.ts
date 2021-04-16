import { Injectable } from "@nestjs/common";
import { Prisma, Role, User as PrismaUser } from "@prisma/client";

interface User extends PrismaUser {
  roles: Role[];
}

import { CrudRepository } from "../config/crud-service.interface";
import { PrismaService } from "../prisma.service";

@Injectable()
export class UserService implements CrudRepository<User | PrismaUser> {
  constructor(private readonly prisma: PrismaService) {}

  create(data: Prisma.UserCreateInput): Promise<PrismaUser> {
    return this.prisma.user.create({ data });
  }

  update(where: Prisma.UserWhereUniqueInput, data: Prisma.UserUpdateInput): Promise<PrismaUser> {
    return this.prisma.user.update({ where, data });
  }

  async delete(where: Prisma.UserWhereUniqueInput): Promise<boolean> {
    try {
      await this.prisma.user.delete({ where });
      return true;
    } catch {
      return false;
    }
  }

  async findOne(where: Prisma.UserWhereUniqueInput): Promise<User | null> {
    return this.prisma.user.findUnique({
      where,
      include: {
        roles: true,
      },
    });
  }

  findMany(where: Prisma.UserWhereInput): Promise<PrismaUser[]> {
    return this.prisma.user.findMany({ where });
  }
}
