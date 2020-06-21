import { NestFactory } from "@nestjs/core";
import { FastifyAdapter, NestFastifyApplication } from "@nestjs/platform-fastify";
import { AppModule } from "./app.module";
import { ConfigService } from "@nestjs/config";

async function bootstrap(): Promise<void> {
  const app = await NestFactory.create<NestFastifyApplication>(AppModule, new FastifyAdapter());
  const configService = app.get(ConfigService);
  const port = configService.get<number>("PORT") ?? 3000;
  await app.listen(port, "0.0.0.0");

  console.log(`env: ${process.env.NODE_ENV ?? "wat"}`);
  console.log(`Application is running on: ${await app.getUrl()}`);
}

bootstrap()
  .then(() => null)
  .catch((e) => console.error(e));
