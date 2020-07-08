import Store from "connect-redis";
import expressSession from "express-session";
import Redis from "ioredis";
import { SessionService } from "./session.service";

export const SessionProvider = {
  provide: "SessionProvider",
  useFactory: (sessionService: SessionService): unknown => {
    const redis = new Redis();
    const RedisStore = Store(expressSession);
    const store = new RedisStore({
      client: redis,
    });

    const options = sessionService.options(store);

    return expressSession(options);
  },
  inject: [SessionService],
};
