import { GqlModuleOptions } from "@nestjs/graphql";
import { registerAs } from "@nestjs/config";

export default registerAs(
  "graphql",
  (): GqlModuleOptions => ({
    debug: (process.env.PRODUCTION ?? "true") === "true",
    playground: (process.env.PRODUCTION ?? "true") === "true",
    introspection: (process.env.PRODUCTION ?? "true") === "true",
  }),
);
