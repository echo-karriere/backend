import { Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { CategoryResolver } from "./category.resolver";
import { CategoryService } from "./category.service";

@Module({
  providers: [CategoryService, CategoryResolver, PrismaService],
})
export class CategoryModule {}
