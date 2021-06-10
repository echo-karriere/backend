import { Args, Mutation, Parent, Query, ResolveField, Resolver } from "@nestjs/graphql";

import { ROLES } from "../auth/auth.config";
import { Secured } from "../auth/roles.guard";
import { Job } from "../job/entities/job.entity";
import { JobService } from "../job/job.service";
import { CompanyService } from "./company.service";
import { CreateCompanyInput } from "./dto/create-company.input";
import { UpdateCompanyInput } from "./dto/update-company.input";
import { Company } from "./entities/company.entity";

@Resolver(() => Company)
@Secured(ROLES.ADMIN, ROLES.STAFF)
export class CompanyResolver {
  constructor(private service: CompanyService, private readonly jobService: JobService) {}

  @Query(() => [Company], { name: "companies" })
  async findMany(): Promise<Array<Company>> {
    return this.service.findMany();
  }

  @Query(() => Company, { name: "company", nullable: true })
  async findOne(@Args("id") id: string): Promise<Company | null> {
    return this.service.findOne({ id });
  }

  @Mutation(() => Company, { nullable: true })
  async createCompany(@Args("input") input: CreateCompanyInput): Promise<Company> {
    return this.service.create({
      name: input.name,
      homepage: input.homepage,
    });
  }

  @Mutation(() => Company, { nullable: true })
  async updateCompany(@Args("id") id: string, @Args("input") input: UpdateCompanyInput): Promise<Company> {
    return this.service.update(
      { id },
      {
        name: input.name,
        homepage: input.homepage,
        modifiedAt: new Date(),
      },
    );
  }

  @Mutation(() => Boolean)
  async deleteCompany(@Args("id") id: string): Promise<boolean> {
    return this.service.delete({ id });
  }

  @ResolveField("jobs", () => [Job])
  async companyJobs(@Parent() company: Company): Promise<Array<Job>> {
    const { id } = company;
    const jobs = await this.jobService.findCompanyJob(id);
    return jobs.map((job) => {
      return { ...job, type: this.jobService.typeToEntity(job.type) };
    });
  }
}
