import { Injectable } from "@nestjs/common";
import { PassportStrategy } from "@nestjs/passport";
import { BearerStrategy } from "passport-azure-ad";
import * as process from "process";

@Injectable()
export class AADB2CStrategy extends PassportStrategy(BearerStrategy, "aad") {
  constructor() {
    super({
      identityMetadata: `https://${process.env.AZURE_TENANT_NAME}.b2clogin.com/${process.env.AZURE_TENANT_NAME}.onmicrosoft.com/${process.env.AZURE_POLICY_NAME}/v2.0/.well-known/openid-configuration`,
      clientID: process.env.AZURE_CLIENT_ID,
      audience: process.env.AZURE_CLIENT_ID,
      policyName: process.env.AZURE_POLICY_NAME,
      isB2C: true,
      validateIssuer: true,
      loggingLevel: "warn",
      passReqToCallback: true,
    });
  }

  // eslint-disable-next-line @typescript-eslint/require-await
  async validate(_: unknown, token: unknown): Promise<unknown> {
    return token;
  }
}
