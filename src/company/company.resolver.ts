import { UseGuards } from "@nestjs/common";
import { Args, Mutation, Query, Resolver } from "@nestjs/graphql";

import { GqlAuthGuard } from "../auth/gql.guard";
import { CompanyService } from "./company.service";
import { CreateCompanyInput } from "./dto/create-company.input";
import { UpdateCompanyInput } from "./dto/update-company.input";
import { Company } from "./models/company.model";

@Resolver(() => Company)
@UseGuards(GqlAuthGuard)
export class CompanyResolver {
  constructor(private service: CompanyService) {}

  @Query(() => [Company])
  async allCompanies(): Promise<Array<Company>> {
    return this.service.selectAll();
  }

  @Query(() => Company, { nullable: true })
  async companyById(@Args("id") id: string): Promise<Company | null> {
    return this.service.select({ id });
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
}
