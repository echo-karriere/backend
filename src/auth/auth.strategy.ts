import { Injectable, UnauthorizedException } from "@nestjs/common";
import { PassportStrategy } from "@nestjs/passport";
import { BearerStrategy } from "passport-azure-ad";

import { AzureToken } from "../user/entities/token.entity";
import { UserService } from "../user/user.service";

@Injectable()
export class AADB2CStrategy extends PassportStrategy(BearerStrategy, "aad") {
  constructor(private readonly userService: UserService) {
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

  async validate(_: unknown, token: AzureToken): Promise<AzureToken & { roles: string[] }> {
    const user = await this.userService.findOne({ id: token.oid });

    if (user === null) {
      throw new UnauthorizedException();
    }

    const result = Object.assign(token, { roles: user.roles.map((r) => r.name) });
    // console.log(result);
    return result;
  }
}
