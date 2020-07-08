import { Module } from "@nestjs/common";
import { ConfigModule } from "@nestjs/config";
import { SessionProvider } from "./session.provider";
import { SessionService } from "./session.service";

@Module({
  imports: [ConfigModule],
  providers: [SessionService, SessionProvider],
  exports: [SessionProvider],
})
export class SessionModule {}
