import { Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { JobResolver } from "./job.resolver";
import { JobService } from "./job.service";

@Module({
  providers: [JobResolver, JobService, PrismaService],
})
export class JobModule {}
