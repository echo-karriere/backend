import { Injectable } from "@nestjs/common";
import { Company, Prisma } from "@prisma/client";

import { PrismaService } from "../prisma.service";

@Injectable()
export class CompanyService {
  constructor(private readonly prisma: PrismaService) {}

  async create(data: Prisma.CompanyCreateInput): Promise<Company> {
    return this.prisma.company.create({
      data,
    });
  }

  async update(where: Prisma.CompanyWhereUniqueInput, data: Prisma.CompanyUpdateInput): Promise<Company> {
    return this.prisma.company.update({
      where,
      data,
    });
  }

  async delete(where: Prisma.CompanyWhereUniqueInput): Promise<boolean> {
    try {
      await this.prisma.company.delete({
        where,
      });

      return true;
    } catch {
      return false;
    }
  }

  async findOne(where: Prisma.CompanyWhereUniqueInput): Promise<Company | null> {
    return this.prisma.company.findUnique({
      where,
    });
  }

  async findMany(where?: Prisma.CompanyWhereInput): Promise<Array<Company>> {
    return this.prisma.company.findMany({ where });
  }
}
