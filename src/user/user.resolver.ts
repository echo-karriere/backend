import { UseGuards } from "@nestjs/common";
import { Args, Mutation, Query, Resolver } from "@nestjs/graphql";

import { ROLES } from "../auth/auth.config";
import { GqlAuthGuard } from "../auth/gql.guard";
import { Roles, Secured } from "../auth/roles.guard";
import { CreateUserInput } from "./dto/create-user.input";
import { UpdateUserInput } from "./dto/update-user.input";
import { AzureToken } from "./entities/token.entity";
import { User } from "./entities/user.entity";
import { CurrentUser } from "./user.decorator";
import { UserService } from "./user.service";

@Resolver(() => User)
@Secured(ROLES.ADMIN, ROLES.STAFF)
export class UserResolver {
  constructor(private service: UserService) {}

  @Query(() => User, { nullable: true })
  @Roles(ROLES.USER)
  @UseGuards(GqlAuthGuard)
  async me(@CurrentUser() user: AzureToken): Promise<Omit<User, "enabled"> | null> {
    const data = await this.service.findOne({ id: user.oid });

    if (data === null || !data.enabled) return null;

    return {
      id: data.id,
      name: data.name,
      email: data.email,
      roles: data.roles,
    };
  }

  @Query(() => [User], { name: "users" })
  async findMany(): Promise<Array<User>> {
    return this.service.findMany({}, { roles: true });
  }

  @Query(() => User, { name: "user", nullable: true })
  async findOne(@Args("id") id: string): Promise<User | null> {
    return this.service.findOne({ id });
  }

  @Mutation(() => User, { nullable: true })
  async createUser(@Args("input") input: CreateUserInput): Promise<User> {
    return this.service.create({
      name: input.name,
      email: input.email,
      enabled: true,
      roles: {
        connect: input.roles.map((role) => {
          return { id: role };
        }),
      },
    });
  }

  @Mutation(() => User, { nullable: true })
  async updateUser(@Args("id") id: string, @Args("input") input: UpdateUserInput): Promise<User> {
    const user = await this.service.findOne({ id: id });

    const lostRoles = user.roles.filter((role) => !input.roles.includes(role.id)).map((role) => role.id);
    const newRoles = input.roles.filter((role) => !user.roles.some((r) => r.id === role));

    await this.service.updateRoles(newRoles, lostRoles, id);
    return this.service.update(
      { id },
      {
        name: input.name,
        email: input.email,
        enabled: input.enabled,
        roles: {
          disconnect: lostRoles.map((role) => {
            return { id: role };
          }),
          connect: input.roles.map((role) => {
            return { id: role };
          }),
        },
      },
    );
  }

  @Mutation(() => Boolean)
  async deleteUser(@Args("id") id: string): Promise<boolean> {
    return this.service.delete({ id });
  }
}
