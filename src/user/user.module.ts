import { Module } from "@nestjs/common";
import { UserResolver } from "./user.resolver";
import { PrismaModule } from "../prisma/prisma.module";
import { AuthModule } from "../auth/auth.module";

@Module({
  providers: [UserResolver],
  exports: [UserResolver],
  imports: [PrismaModule, AuthModule],
})
export class UserModule {}
