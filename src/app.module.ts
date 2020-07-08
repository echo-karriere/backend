import Joi from "@hapi/joi";
import { Module } from "@nestjs/common";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { GraphQLModule } from "@nestjs/graphql";
import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { AuthModule } from "./auth/auth.module";
import configurations from "./config";
import { PrismaModule } from "./prisma/prisma.module";
import { UserModule } from "./user/user.module";

@Module({
  imports: [
    ConfigModule.forRoot({
      envFilePath: [".env.development", ".env.production"],
      load: configurations,
      validationSchema: Joi.object({
        NODE_ENV: Joi.string().valid("development", "production", "test").default("development"),
        PORT: Joi.number().default(3000),
      }),
      validationOptions: {
        allowUnknown: true,
        abortEarly: true,
      },
    }),
    GraphQLModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: async (config: ConfigService) => config.get("graphql") ?? {},
    }),
    AuthModule,
    UserModule,
    PrismaModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
