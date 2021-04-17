import { HttpModule, Module } from "@nestjs/common";
import { PassportModule } from "@nestjs/passport";

import { AzureService } from "../azure/azure.service";
import { GraphService } from "../azure/graph.service";
import { PrismaService } from "../prisma.service";
import { UserService } from "../user/user.service";
import { AADB2CStrategy } from "./auth.strategy";

@Module({
  imports: [PassportModule, HttpModule],
  providers: [AADB2CStrategy, UserService, PrismaService, AzureService, GraphService],
})
export class AuthModule {}
