import { Injectable } from "@nestjs/common";
import { ConfigService } from "@nestjs/config";
import Store from "connect-redis";
import expressSession from "express-session";

@Injectable()
export class SessionService {
  constructor(private readonly configService: ConfigService) {}

  options(store: Store.RedisStore): expressSession.SessionOptions {
    return {
      store,
      name: this.configService.get<string>("session_name") ?? "session",
      secret: this.configService.get<string>("session_secret") ?? "secret",
      resave: false,
      saveUninitialized: false,
      cookie: {
        httpOnly: this.configService.get<boolean>("production") ?? true,
        secure: this.configService.get<boolean>("production") ?? true,
        maxAge: this.configService.get<number>("session_age") ?? 3600,
      },
    };
  }
}
