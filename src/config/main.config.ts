import { registerAs } from "@nestjs/config";

export interface MainConfig {
  port: number;
  production: boolean;
}
export default registerAs(
  "main",
  (): MainConfig => ({
    port: parseInt(process.env.PORT ?? "3000", 10),
    production: (process.env.PRODUCTION ?? "true") === "true",
  }),
);
