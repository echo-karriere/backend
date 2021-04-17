import { HttpModule, Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { AzureResolver } from "./azure.resolver";
import { AzureService } from "./azure.service";
import { GraphService } from "./graph.service";

@Module({
  imports: [HttpModule],
  providers: [AzureResolver, AzureService, GraphService, PrismaService],
})
export class AzureModule {}
