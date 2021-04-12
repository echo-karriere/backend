import { Module } from "@nestjs/common";
import { TerminusModule } from "@nestjs/terminus";

import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { HealthController } from "./health/health.controller";
import { PrismaService } from "./prisma.service";

@Module({
  imports: [TerminusModule],
  controllers: [AppController, HealthController],
  providers: [PrismaService, AppService],
})
export class AppModule {}
