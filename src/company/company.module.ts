import { Module } from "@nestjs/common";

import { JobService } from "../job/job.service";
import { PrismaService } from "../prisma.service";
import { CompanyResolver } from "./company.resolver";
import { CompanyService } from "./company.service";

@Module({
  providers: [CompanyService, CompanyResolver, PrismaService, JobService],
})
export class CompanyModule {}
