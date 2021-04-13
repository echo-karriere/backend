import { UseGuards } from "@nestjs/common";
import { Args, Mutation, Query, Resolver } from "@nestjs/graphql";

import { GqlAuthGuard } from "../auth/gql.guard";
import { CategoryService } from "./category.service";
import { CreateCategoryInput } from "./dto/create-category.input";
import { UpdateCategoryInput } from "./dto/update-category.input";
import { Category } from "./models/category.model";

@Resolver(() => Category)
@UseGuards(GqlAuthGuard)
export class CategoryResolver {
  constructor(private service: CategoryService) {}

  @Query(() => [Category])
  async allCategories(): Promise<Array<Category>> {
    return this.service.selectAll();
  }

  @Query(() => Category, { nullable: true })
  async categoryById(@Args("id") id: string): Promise<Category | null> {
    return this.service.select({ id });
  }

  @Mutation(() => Category, { nullable: true })
  async createCategory(@Args("input") input: CreateCategoryInput): Promise<Category> {
    return this.service.create({
      title: input.title,
      description: input.description,
      slug: input.slug,
    });
  }

  @Mutation(() => Category, { nullable: true })
  async updateCategory(@Args("id") id: string, @Args("input") input: UpdateCategoryInput): Promise<Category> {
    return this.service.update(
      { id },
      {
        title: input.title,
        description: input.description,
        slug: input.slug,
        modifiedAt: new Date(),
      },
    );
  }

  @Mutation(() => Boolean)
  async deleteCategory(@Args("id") id: string): Promise<boolean> {
    return this.service.delete({ id });
  }
}
