import { registerAs } from "@nestjs/config";

export interface AuthConfig {
  jwtToken: string;
  jwtExpiry: string;
}

export default registerAs(
  "auth",
  (): AuthConfig => ({
    jwtToken: process.env.JWT_TOKEN ?? "pleasechangeme",
    jwtExpiry: process.env.JET_EXPIRY ?? "60s",
  }),
);
