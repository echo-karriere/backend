import { Module } from "@nestjs/common";
import { GraphQLModule } from "@nestjs/graphql";
import { TerminusModule } from "@nestjs/terminus";

import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { HealthController } from "./health/health.controller";
import { PrismaService } from "./prisma.service";

@Module({
  imports: [
    TerminusModule,
    GraphQLModule.forRoot({
      autoSchemaFile: "schema.gql",
      installSubscriptionHandlers: true,
      sortSchema: true,
    }),
  ],
  controllers: [AppController, HealthController],
  providers: [PrismaService, AppService],
})
export class AppModule {}
