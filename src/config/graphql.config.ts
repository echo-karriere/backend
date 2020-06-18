import { GqlModuleOptions } from "@nestjs/graphql";
import { registerAs } from "@nestjs/config";
import { join } from "path";

export default registerAs(
  "graphql",
  (): GqlModuleOptions => ({
    debug: !((process.env.PRODUCTION ?? "true") === "true"),
    playground: !((process.env.PRODUCTION ?? "true") === "true"),
    introspection: !((process.env.PRODUCTION ?? "true") === "true"),
    autoSchemaFile: join(process.cwd(), "src/schema.gql"),
  }),
);
