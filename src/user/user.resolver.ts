import { UseGuards } from "@nestjs/common";
import { Query, Resolver } from "@nestjs/graphql";

import { GqlAuthGuard } from "../auth/gql.guard";
import { AzureToken } from "./entities/token.entity";
import { User } from "./entities/user.entity";
import { CurrentUser } from "./user.decorator";
import { UserService } from "./user.service";

@Resolver(() => User)
export class UserResolver {
  constructor(private service: UserService) {}

  @Query(() => User, { nullable: true })
  @UseGuards(GqlAuthGuard)
  async me(@CurrentUser() user: AzureToken): Promise<User | null> {
    try {
      const data = await this.service.findOne({ id: user.oid });
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
