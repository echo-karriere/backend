import { Query, Resolver } from "@nestjs/graphql";

import { AppService, BuildInfo } from "./app.service";

@Resolver(() => BuildInfo)
export class AppResolver {
  constructor(private readonly service: AppService) {}

  @Query(() => BuildInfo)
  buildInfo(): BuildInfo {
    return this.service.getBuildInfo();
  }
}
