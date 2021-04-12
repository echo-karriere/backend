import { Injectable } from "@nestjs/common";
import { Category, Prisma } from "@prisma/client";
import { PrismaService } from "src/prisma.service";

@Injectable()
export class CategoryService {
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

  async select(where: Prisma.CategoryWhereUniqueInput): Promise<Category | null> {
    return this.prisma.category.findUnique({
      where,
    });
  }

  async selectAll(where?: Prisma.CategoryWhereInput): Promise<Array<Category>> {
    return this.prisma.category.findMany({ where });
  }
}
