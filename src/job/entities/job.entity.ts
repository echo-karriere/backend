import { Field, HideField, ID, ObjectType, registerEnumType } from "@nestjs/graphql";

import { Company } from "../../company/entities/company.entity";

export enum JobType {
  Full,
  Part,
  Summer,
  Other,
}

registerEnumType(JobType, { name: "JobType", description: "What type of job is it?" });

@ObjectType({
  description: "A job",
})
export class Job {
  @Field(() => ID, { description: "Job ID" })
  id!: string;

  @Field({ description: "Title of job" })
  title!: string;

  @Field({ description: "Description of job" })
  description!: string;

  @Field({ description: "Where is the job?" })
  location!: string;

  @Field({ description: "External URL for job" })
  url!: string;

  @Field(() => JobType, { description: "What kind of job it is" })
  type!: JobType;

  @Field({ description: "Whether the job posting is published" })
  published: boolean;

  @Field({ description: "When is the deadline for applying?" })
  deadline?: Date;

  @Field({ description: "When should the job be unlisted?" })
  finalExpiration?: Date;

  @Field(() => Company, { description: "Company for job listing" })
  company!: Company;

  @HideField()
  companyId!: string;
}
