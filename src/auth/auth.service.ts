import { Injectable } from "@nestjs/common";
// import { UserResolver } from "../user/user.resolver";
import { JwtService } from "@nestjs/jwt";
import { ConfigService } from "@nestjs/config";
import { JwtToken } from "./models/jwt.model";

@Injectable()
export class AuthService {
  constructor(
    // private userService: UserResolver,
    private jwtService: JwtService,
    private readonly configService: ConfigService,
  ) {}

  validateToken(token: string): boolean {
    return token !== null;
  }

  async generateToken(payload: Record<string, unknown>): Promise<JwtToken> {
    const accessToken = await this.jwtService.signAsync(payload);
    const refreshToken = await this.jwtService.signAsync(payload, {
      expiresIn: this.configService.get<string>("JWT_EXPIRY"),
    });

    return { accessToken, refreshToken };
  }
}
