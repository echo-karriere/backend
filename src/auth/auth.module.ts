import { Module } from "@nestjs/common";
import { PassportModule } from "@nestjs/passport";
import { AuthService } from "./auth.service";
import { JwtModule } from "@nestjs/jwt";
import { JwtStrategy } from "./jwt.strategy";
import { ConfigModule } from "@nestjs/config";
import { jwtConstants } from "./constants";

@Module({
  imports: [
    PassportModule,
    ConfigModule,
    JwtModule.register({
      secret: jwtConstants.jwtSecret,
      signOptions: { expiresIn: jwtConstants.jwtExpiry },
    }),
  ],
  providers: [AuthService, JwtStrategy],
  exports: [AuthService],
})
export class AuthModule {}
