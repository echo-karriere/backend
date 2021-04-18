import { Args, ID, Query, Resolver } from "@nestjs/graphql";

import { Role } from "./entities/role.entity";
import { RoleService } from "./role.service";

@Resolver(() => Role)
export class RoleResolver {
  constructor(private readonly roleService: RoleService) {}

  @Query(() => [Role], { name: "roles" })
  findAll(): Promise<Role[]> {
    return this.roleService.findAll({});
  }

  @Query(() => Role, { name: "role" })
  findOne(@Args("id", { type: () => ID }) id: string): Promise<Role | null> {
    return this.roleService.findOne({ id });
  }
}
