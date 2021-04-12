import { Args, Mutation, Query, Resolver } from "@nestjs/graphql";

import { CategoryService } from "./category.service";
import { CreateCategoryInput, UpdateCategoryInput } from "./dto";
import { Category } from "./models";

@Resolver(() => Category)
export class CategoryResolver {
  constructor(private service: CategoryService) {}

  @Query(() => [Category])
  async allCategories(): Promise<Array<Category>> {
    return this.service.selectAll();
  }

  @Query(() => Category, { nullable: true })
  async categoryById(@Args("id") id: string): Promise<Category> {
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
