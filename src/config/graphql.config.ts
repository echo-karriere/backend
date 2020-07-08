import { registerAs } from "@nestjs/config";
import { GqlModuleOptions } from "@nestjs/graphql";
import { Request, Response } from "express";
import { join } from "path";

export default registerAs(
  "graphql",
  (): GqlModuleOptions => ({
    context: ({ req, res }: { req: Request; res: Response }) => ({ req, res }),
    debug: process.env.NODE_ENV === "development",
    tracing: process.env.NODE_ENV === "development",
    playground: process.env.NODE_ENV === "development",
    introspection: true,
    autoSchemaFile: join(process.cwd(), "src/schema.graphql"),
  }),
);
