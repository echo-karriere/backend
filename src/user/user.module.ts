import { Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { UserResolver } from "./user.resolver";
import { UserService } from "./user.service";

@Module({
  providers: [UserService, PrismaService, UserResolver],
})
export class UserModule {}
