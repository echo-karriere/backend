import { Mutation, Resolver } from "@nestjs/graphql";

import { MsalService } from "./msal.service";

@Resolver()
export class MsalResolver {
  constructor(private service: MsalService) {}

  @Mutation(() => Boolean)
  async updateUsersFromMsal(): Promise<boolean> {
    await this.service.getUsers();
    return true;
  }
}
