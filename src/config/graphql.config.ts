import { GqlModuleOptions } from "@nestjs/graphql";
import { registerAs } from "@nestjs/config";
import { join } from "path";

export default registerAs(
  "graphql",
  (): GqlModuleOptions => ({
    debug: process.env.NODE_ENV === "development",
    playground: process.env.NODE_ENV === "development",
    introspection: process.env.NODE_ENV === "development",
    autoSchemaFile: join(process.cwd(), "src/schema.graphql"),
  }),
);
