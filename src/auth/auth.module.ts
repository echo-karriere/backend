import { Module } from "@nestjs/common";
import { ConfigModule } from "@nestjs/config";
import { PassportModule } from "@nestjs/passport";
import { PrismaModule } from "../prisma/prisma.module";
import { AuthResolver } from "./auth.resolver";
import { AuthService } from "./auth.service";
import { LocalStrategy } from "./local.strategy";

@Module({
  imports: [PrismaModule, PassportModule, ConfigModule],
  providers: [AuthService, AuthResolver, LocalStrategy],
  exports: [AuthService, AuthResolver],
})
export class AuthModule {}
