import { Injectable } from "@nestjs/common";
import { Category, Prisma } from "@prisma/client";

import { CrudRepository } from "../config/crud-service.interface";
import { PrismaService } from "../prisma.service";

@Injectable()
export class CategoryService implements CrudRepository<Category> {
  constructor(private readonly prisma: PrismaService) {}

  async create(data: Prisma.CategoryCreateInput): Promise<Category> {
    return this.prisma.category.create({
      data,
    });
  }

  async update(where: Prisma.CategoryWhereUniqueInput, data: Prisma.CategoryUpdateInput): Promise<Category> {
    return this.prisma.category.update({
      where,
      data,
    });
  }

  async delete(where: Prisma.CategoryWhereUniqueInput): Promise<boolean> {
    try {
      await this.prisma.category.delete({
        where,
      });

      return true;
    } catch {
      return false;
    }
  }

  async findOne(where: Prisma.CategoryWhereUniqueInput): Promise<Category | null> {
    return this.prisma.category.findUnique({
      where,
    });
  }

  async findMany(where?: Prisma.CategoryWhereInput): Promise<Array<Category>> {
    return this.prisma.category.findMany({ where });
  }
}
