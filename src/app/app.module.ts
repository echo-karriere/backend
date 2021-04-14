import { Module } from "@nestjs/common";
import { GraphQLModule } from "@nestjs/graphql";
import { TerminusModule } from "@nestjs/terminus";

import { AuthModule } from "../auth/auth.module";
import { CategoryModule } from "../category/category.module";
import { CompanyModule } from "../company/company.module";
import { corsConfiguration } from "../config/cors.config";
import { HealthController } from "../health/health.controller";
import { UserModule } from "../user/user.module";
import { AppController } from "./app.controller";
import { AppResolver } from "./app.resolver";
import { AppService } from "./app.service";

@Module({
  imports: [
    GraphQLModule.forRoot({
      autoSchemaFile: "schema.gql",
      installSubscriptionHandlers: true,
      sortSchema: true,
      cors: corsConfiguration,
      context: ({ req }: { req: unknown }) => ({ req }),
    }),
    TerminusModule,
    CategoryModule,
    CompanyModule,
    AuthModule,
    UserModule,
  ],
  controllers: [AppController, HealthController],
  providers: [AppService, AppResolver],
})
export class AppModule {}
