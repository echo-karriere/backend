import { HttpModule, Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { MsalResolver } from "./azure.resolver";
import { MsalService } from "./azure.service";
import { GraphService } from "./graph.service";

@Module({
  imports: [HttpModule],
  providers: [MsalResolver, MsalService, GraphService, PrismaService],
})
export class MsalModule {}
