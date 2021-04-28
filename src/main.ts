import { NestFactory } from "@nestjs/core";
import helmet from "helmet";

import { AppModule } from "./app/app.module";
import { corsConfiguration } from "./config/cors.config";

async function bootstrap() {
  const app = await NestFactory.create(AppModule, { cors: corsConfiguration });

  const port = process.env.PORT ? Number.parseInt(process.env.PORT, 10) : 8080;

  app.use(
    helmet({
      contentSecurityPolicy: {
        directives: {
          defaultSrc: [`'self'`],
          styleSrc: [`'self'`, `'unsafe-inline'`, "cdn.jsdelivr.net", "fonts.googleapis.com"],
          fontSrc: [`'self'`, "fonts.gstatic.com"],
          imgSrc: [`'self'`, "data:", "cdn.jsdelivr.net"],
          scriptSrc: [`'self'`, `https: 'unsafe-inline'`, `cdn.jsdelivr.net`],
        },
      },
    }),
  );

  await app.listen(port, "0.0.0.0");
  console.log(`Application is running on: ${await app.getUrl()}`);
}

void bootstrap();
