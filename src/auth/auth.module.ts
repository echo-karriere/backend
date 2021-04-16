import { Module } from "@nestjs/common";
import { PassportModule } from "@nestjs/passport";

import { PrismaService } from "../prisma.service";
import { UserService } from "../user/user.service";
import { AADB2CStrategy } from "./auth.strategy";

@Module({
  imports: [PassportModule],
  providers: [AADB2CStrategy, UserService, PrismaService],
})
export class AuthModule {}
