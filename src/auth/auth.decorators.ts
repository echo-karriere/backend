import { Response } from "express";
import { createParamDecorator, ExecutionContext } from "@nestjs/common";
import { GqlExecutionContext } from "@nestjs/graphql";

export const GqlRes = createParamDecorator(
  (data: unknown, context: ExecutionContext): Response =>
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return, @typescript-eslint/no-unsafe-member-access
    GqlExecutionContext.create(context).getContext().res,
);
