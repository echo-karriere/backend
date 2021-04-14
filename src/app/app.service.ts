import { Injectable } from "@nestjs/common";
import { Field, ObjectType } from "@nestjs/graphql";

import data from "../config/build.json";

@ObjectType({ description: "Information used to check application state" })
export class BuildInfo {
  @Field({ description: "Commit hash for the app" })
  commit: string;
  @Field({ description: "Date and time when app was built" })
  buildDate: Date;
}

@Injectable()
export class AppService {
  getHello(): string {
    return "Hello World!";
  }

  getBuildInfo(): BuildInfo {
    return {
      commit: data.commit,
      buildDate: new Date(data.buildDate),
    };
  }
}
