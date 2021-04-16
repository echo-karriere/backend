import {
  applyDecorators,
  CanActivate,
  CustomDecorator,
  ExecutionContext,
  Injectable,
  SetMetadata,
  UseGuards,
} from "@nestjs/common";
import { Reflector } from "@nestjs/core";
import { GqlExecutionContext } from "@nestjs/graphql";
import { Observable } from "rxjs";

import { User } from "../user/entities/user.entity";
import { GqlAuthGuard } from "./gql.guard";

type NestCustomDecorator = ReturnType<typeof applyDecorators>;

/**
 * Secure a resolver or controller, this annotation forces the user to be authenticated
 * and to have one of the roles to access the function. This is a shorthand for adding
 * `@UseGuards(GqlAuthGuard)` and `@Roles(ROLES.ADMIN, ROLES.STAFF)`.
 * @param roles - Allowed roles for this resolver/controller.
 */
export const Secured = (...roles: string[]): NestCustomDecorator => {
  return applyDecorators(Roles(...roles), UseGuards(GqlAuthGuard, RolesGuard));
};

export const Roles = (...roles: string[]): CustomDecorator => SetMetadata("roles", roles);

@Injectable()
export class RolesGuard implements CanActivate {
  constructor(private reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean | Promise<boolean> | Observable<boolean> {
    const roles = this.reflector.getAllAndOverride<string[]>("roles", [context.getHandler(), context.getClass()]);

    if (!roles) return true;

    const ctx = GqlExecutionContext.create(context);
    const req = ctx.getContext<Record<string, Record<string, unknown>>>().req;
    const user = req.user as User;
    const hasRoles = user.roles.some((role) => roles.includes((role as unknown) as string));

    return req && user && hasRoles;
  }
}
