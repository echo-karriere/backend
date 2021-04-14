import { Module } from "@nestjs/common";

import { PrismaService } from "../prisma.service";
import { MsalResolver } from "./msal.resolver";
import { MsalService } from "./msal.service";

@Module({
  providers: [MsalResolver, MsalService, PrismaService],
})
export class MsalModule {}
