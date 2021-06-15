import { Args, Mutation, Query, Resolver } from "@nestjs/graphql";

import { ROLES } from "../auth/auth.config";
import { Secured } from "../auth/roles.guard";
import { CreateJobInput } from "./dto/create-job.input";
import { UpdateJobInput } from "./dto/update-job.input";
import { Job } from "./entities/job.entity";
import { JobService } from "./job.service";

@Resolver(() => Job)
@Secured(ROLES.ADMIN, ROLES.STAFF)
export class JobResolver {
  constructor(private readonly service: JobService) {}

  @Mutation(() => Job)
  async createJob(@Args("input") input: CreateJobInput): Promise<Job> {
    const createdJob = await this.service.create({
      title: input.title,
      description: input.description,
      location: input.location,
      url: input.url,
      type: this.service.typeToPrisma(input.type),
      published: input.published,
      deadline: input.deadline,
      finalExpiration: input.finalExpiration,
      company: {
        connect: {
          id: input.company,
        },
      },
    });

    return { ...createdJob, type: this.service.typeToEntity(createdJob.type) };
  }

  @Query(() => [Job], { name: "jobs" })
  async findMany(): Promise<Array<Job>> {
    const jobs = await this.service.findMany();
    return jobs.map((job) => {
      return { ...job, type: this.service.typeToEntity(job.type) };
    });
  }

  @Query(() => Job, { name: "job", nullable: true })
  async findOne(@Args("id") id: string): Promise<Job> {
    const job = await this.service.findOne({ id });
    return { ...job, type: this.service.typeToEntity(job.type) };
  }

  @Mutation(() => Job, { nullable: true })
  async updateJob(@Args("id") id: string, @Args("input") input: UpdateJobInput): Promise<Job> {
    const updatedJob = await this.service.update(
      { id },
      {
        title: input.title,
        description: input.description,
        location: input.location,
        url: input.url,
        type: this.service.typeToPrisma(input.type),
        published: input.published,
        deadline: input.deadline,
        finalExpiration: input.finalExpiration,
        company: {
          connect: {
            id: input.company,
          },
        },
      },
    );

    return { ...updatedJob, type: this.service.typeToEntity(updatedJob.type) };
  }

  @Mutation(() => Boolean)
  deleteJob(@Args("id") id: string): Promise<boolean> {
    return this.service.delete({ id });
  }
}
