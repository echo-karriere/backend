import { Injectable } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import { ConfigService } from "@nestjs/config";

@Injectable()
export class AuthService {
  constructor(private jwtService: JwtService, private readonly configService: ConfigService) {}

  validateToken(token: string): boolean {
    return token !== null;
  }

  async generateToken(payload: Record<string, unknown>): Promise<{ accessToken: string; refreshToken: string }> {
    const accessToken = await this.jwtService.signAsync(payload);
    const refreshToken = await this.jwtService.signAsync(payload, {
      expiresIn: this.configService.get<string>("JWT_EXPIRY"),
    });

    return { accessToken, refreshToken };
  }
}
