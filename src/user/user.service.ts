import { Injectable } from "@nestjs/common";
import { Prisma, Role, User as PrismaUser } from "@prisma/client";

import { AzureService } from "../azure/azure.service";

interface User extends PrismaUser {
  roles: Role[];
}

import { CrudRepository } from "../config/crud-service.interface";
import { PrismaService } from "../prisma.service";

@Injectable()
export class UserService implements CrudRepository<User | PrismaUser> {
  constructor(private readonly prisma: PrismaService, private azure: AzureService) {}

  async create(data: Prisma.UserCreateInput): Promise<PrismaUser> {
    const user = await this.azure.createUser({ name: data.name, email: data.email });
    return this.prisma.user.create({
      data: {
        id: user.id,
        name: user.displayName,
        email: user.mail,
        enabled: true,
        roles: data.roles,
      },
      include: {
        roles: true,
      },
    });
  }

  async updateRoles(newRoles: string[], lostRoles: string[], userId: string): Promise<void> {
    try {
      await Promise.all(lostRoles.map((group) => this.azure.removeMembersFromGroup(group, userId)));
      await Promise.all(newRoles.map((group) => this.azure.addMembersToGroup(group, userId)));
    } catch (error) {
      console.log(error);
      throw error;
    }
  }

  async update(where: Prisma.UserWhereUniqueInput, data: Prisma.UserUpdateInput): Promise<PrismaUser> {
    await this.azure.updateUser(where.id, {
      name: data.name?.toString(),
      email: data.email?.toString(),
      enabled: data.enabled?.valueOf() as boolean,
    });
    return this.prisma.user.update({ where, data, include: { roles: true } });
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

  findMany(where: Prisma.UserWhereInput, include: Prisma.UserInclude = {}): Promise<PrismaUser[]> {
    return this.prisma.user.findMany({ where, include });
  }
}
