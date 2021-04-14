import { createParamDecorator, ExecutionContext } from "@nestjs/common";
import { GqlExecutionContext } from "@nestjs/graphql";

import { AzureToken } from "./models/token.model";

export const CurrentUser = createParamDecorator((_: unknown, context: ExecutionContext) => {
  const ctx = GqlExecutionContext.create(context);
  const user = (ctx.getContext<Record<string, Record<string, string>>>().req.user as unknown) as AzureToken;
  return user;
});
