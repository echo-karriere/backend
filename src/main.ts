import { NestFactory } from "@nestjs/core";

import { AppModule } from "./app.module";

async function bootstrap() {
  const app = await NestFactory.create(AppModule, { cors: true });

  const port = process.env.PORT ? Number.parseInt(process.env.PORT, 10) : 8080;

  await app.listen(port, "0.0.0.0");
  console.log(`Application is running on: ${await app.getUrl()}`);

  if (module.hot) {
    module.hot.accept();
    module.hot.dispose(() => void app.close());
  }
}

void bootstrap();
