import { Module } from "@nestjs/common";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { GraphQLModule } from "@nestjs/graphql";
import { AppController } from "./app.controller";
import { AppService } from "./app.service";
import { PrismaService } from "./prisma/prisma.service";
import configurations from "./config";

@Module({
  imports: [
    ConfigModule.forRoot({
      envFilePath: [".env.development", ".env.production"],
      load: configurations,
    }),
    GraphQLModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: async (config: ConfigService) => config.get("graphql") ?? {},
    }),
  ],
  controllers: [AppController],
  providers: [AppService, PrismaService],
})
export class AppModule {}
