import { UseGuards } from "@nestjs/common";
import { Args, Mutation, Query, Resolver } from "@nestjs/graphql";

import { GqlAuthGuard } from "../auth/gql.guard";
import { CategoryService } from "./category.service";
import { CreateCategoryInput } from "./dto/create-category.input";
import { UpdateCategoryInput } from "./dto/update-category.input";
import { Category } from "./entities/category.entity";

@Resolver(() => Category)
@UseGuards(GqlAuthGuard)
export class CategoryResolver {
  constructor(private service: CategoryService) {}

  @Query(() => [Category], { name: "categories" })
  async findMany(): Promise<Array<Category>> {
    return this.service.findMany();
  }

  @Query(() => Category, { name: "category", nullable: true })
  async findOne(@Args("id") id: string): Promise<Category | null> {
    return this.service.findOne({ id });
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
