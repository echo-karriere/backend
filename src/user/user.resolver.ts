import { UseGuards } from "@nestjs/common";
import { Query, Resolver } from "@nestjs/graphql";

import { GqlAuthGuard } from "../auth/gql.guard";
import { AzureToken } from "./models/token.model";
import { User } from "./models/user.model";
import { CurrentUser } from "./user.decorator";
import { UserService } from "./user.service";

@Resolver(() => User)
export class UserResolver {
  constructor(private service: UserService) {}

  @Query(() => User, { nullable: true })
  @UseGuards(GqlAuthGuard)
  async me(@CurrentUser() user: AzureToken): Promise<User | null> {
    try {
      const data = await this.service.select({ id: user.oid });
      const roles = data.roles.filter((r) => r).map((r) => r.name);

      return {
        id: data.id,
        roles,
      };
    } catch {
      return null;
    }
  }
}
