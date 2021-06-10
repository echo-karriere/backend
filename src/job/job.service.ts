import { Injectable } from "@nestjs/common";
import { Job, JobType, Prisma } from "@prisma/client";

import { Company } from "../company/entities/company.entity";
import { CrudRepository } from "../config/crud-service.interface";
import { PrismaService } from "../prisma.service";
import { JobType as EntityJobType } from "./entities/job.entity";

type JobCompany = Job & { company: Company };

@Injectable()
export class JobService implements CrudRepository<Job> {
  constructor(private readonly prisma: PrismaService) {}

  async create(data: Prisma.JobCreateInput): Promise<JobCompany> {
    return this.prisma.job.create({ data, include: { company: true } });
  }

  async delete(where: Prisma.JobWhereUniqueInput): Promise<boolean> {
    try {
      await this.prisma.job.delete({ where });
      return true;
    } catch {
      return false;
    }
  }

  findMany(): Promise<Array<JobCompany>> {
    return this.prisma.job.findMany({ include: { company: true } });
  }

  findOne(where: Prisma.JobWhereUniqueInput): Promise<JobCompany | null> {
    return this.prisma.job.findUnique({ where, include: { company: true } });
  }

  findCompanyJob(companyId: string): Promise<Array<JobCompany>> {
    return this.prisma.job.findMany({
      where: {
        companyId: companyId,
      },
      include: { company: true },
    });
  }

  update(where: Prisma.JobWhereUniqueInput, data: Prisma.JobUpdateInput): Promise<JobCompany> {
    return this.prisma.job.update({ where, data, include: { company: true } });
  }

  typeToEntity(type: JobType): EntityJobType {
    switch (type) {
      case JobType.FULL:
        return EntityJobType.Full;
      case JobType.PART:
        return EntityJobType.Part;
      case JobType.SUMMER:
        return EntityJobType.Summer;
      case JobType.OTHER:
        return EntityJobType.Other;
    }
  }

  typeToPrisma(type: EntityJobType): JobType {
    switch (type) {
      case EntityJobType.Full:
        return JobType.FULL;
      case EntityJobType.Part:
        return JobType.PART;
      case EntityJobType.Summer:
        return JobType.SUMMER;
      case EntityJobType.Other:
        return JobType.OTHER;
    }
  }
}
