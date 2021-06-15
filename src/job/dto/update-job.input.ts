import { Field, InputType } from "@nestjs/graphql";
import { IsOptional } from "class-validator";

import { JobType } from "../entities/job.entity";

@InputType()
export class UpdateJobInput {
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

  @Field()
  published: boolean;

  @Field({ nullable: true })
  @IsOptional()
  deadline?: Date;

  @Field({ nullable: true })
  @IsOptional()
  finalExpiration?: Date;

  @Field()
  company!: string;
}
