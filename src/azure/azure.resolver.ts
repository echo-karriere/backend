import { Mutation, Resolver } from "@nestjs/graphql";

import { ROLES } from "../auth/auth.config";
import { Secured } from "../auth/roles.guard";
import { AzureService } from "./azure.service";

@Resolver()
@Secured(ROLES.ADMIN, ROLES.STAFF)
export class AzureResolver {
  constructor(private service: AzureService) {}

  @Mutation(() => Boolean, {
    description: "Fetch and create or update users from Azure in our system",
  })
  async updateUsersFromAzure(): Promise<boolean> {
    await this.service.getUsers();
    return true;
  }

  @Mutation(() => Boolean, { description: "Fetch and create or update roles from Azure in our system" })
  async updateRolesFromAzure(): Promise<boolean> {
    await this.service.getRoles();
    return true;
  }

  @Mutation(() => Boolean, { description: "Assign roles from Azure to all users in our system" })
  async addRolesToUsers(): Promise<boolean> {
    await this.service.assignRoles();
    return true;
  }
}
