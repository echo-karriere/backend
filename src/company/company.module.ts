import { Module } from "@nestjs/common";
import { PrismaService } from "src/prisma.service";

import { CompanyResolver } from "./company.resolver";
import { CompanyService } from "./company.service";

@Module({
  providers: [CompanyService, CompanyResolver, PrismaService],
})
export class CompanyModule {}
