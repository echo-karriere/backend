import { Field, InputType } from "@nestjs/graphql";
import { IsOptional } from "class-validator";

import { JobType } from "../entities/job.entity";

@InputType()
export class CreateJobInput {
  @Field()
  title!: string;

  @Field()
  description!: string;

  @Field()
  location!: string;

  @Field()
  url!: string;

  @Field(() => JobType)
  type!: JobType;

  @Field({ nullable: true })
  @IsOptional()
  deadline?: Date;

  @Field({ nullable: true })
  @IsOptional()
  finalExpiration?: Date;

  @Field()
  company!: string;
}
