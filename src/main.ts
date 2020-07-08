import { ConfigService } from "@nestjs/config";
import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";

async function bootstrap(): Promise<void> {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);
  const port = configService.get<number>("PORT") ?? 3000;
  await app.listen(port, "0.0.0.0");

  console.log(`Application is running on: ${await app.getUrl()}`);
}

bootstrap()
  .then(() => null)
  .catch((e) => console.error(e));
