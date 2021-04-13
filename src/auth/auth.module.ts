import { Module } from "@nestjs/common";
import { PassportModule } from "@nestjs/passport";

import { AADB2CStrategy } from "./auth.strategy";

@Module({
  imports: [PassportModule],
  providers: [AADB2CStrategy],
})
export class AuthModule {}
