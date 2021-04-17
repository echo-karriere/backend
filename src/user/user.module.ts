import { HttpModule, Module } from "@nestjs/common";

import { AzureService } from "../azure/azure.service";
import { GraphService } from "../azure/graph.service";
import { PrismaService } from "../prisma.service";
import { UserResolver } from "./user.resolver";
import { UserService } from "./user.service";

@Module({
  imports: [HttpModule],
  providers: [UserService, PrismaService, UserResolver, AzureService, GraphService],
})
export class UserModule {}
