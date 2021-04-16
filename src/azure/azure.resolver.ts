import { Mutation, Resolver } from "@nestjs/graphql";

import { MsalService } from "./azure.service";

@Resolver()
export class MsalResolver {
  constructor(private service: MsalService) {}

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
