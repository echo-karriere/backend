import { Controller, Get } from "@nestjs/common";

import { AppService, BuildInfo } from "./app.service";

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getHello(): string {
    return this.appService.getHello();
  }

  @Get("build")
  getBuildInfo(): BuildInfo {
    return this.appService.getBuildInfo();
  }
}
