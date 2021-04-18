import { Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { RoleResolver } from "./role.resolver";
import { RoleService } from "./role.service";

@Module({
  providers: [RoleResolver, RoleService, PrismaService],
})
export class RoleModule {}
