import { PassportStrategy } from "@nestjs/passport";
import { ExtractJwt, Strategy } from "passport-jwt";
import { Injectable, UnauthorizedException } from "@nestjs/common";
import { ConfigService } from "@nestjs/config";

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor(readonly configService: ConfigService) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: configService.get<string>("JWT_TOKEN"),
    });
  }

  // eslint-disable-next-line @typescript-eslint/require-await
  async validate(payload: Record<string, unknown>): Promise<{ id: unknown; email: unknown }> {
    if (!payload || !payload.email) throw new UnauthorizedException();
    return { id: payload.sub, email: payload.email };
  }
}
