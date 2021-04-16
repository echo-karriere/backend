import { Field, ID, ObjectType } from "@nestjs/graphql";

@ObjectType({
  description:
    "A category is used to group pages into logical hierachies, for example by grouping all news related pages under the category 'News'.",
})
export class Category {
  @Field(() => ID, { description: "Category ID" })
  id!: string;

  @Field({ description: "Name of category" })
  title!: string;

  @Field({ nullable: true, description: "What is this category used for?" })
  description?: string;

  @Field({
    description:
      "What is the slug for the URL? For news it could be '/news/' while for the category 'For bedrifter' it could be '/for-bedrifter/'.",
  })
  slug!: string;

  @Field({ description: "When was this resource created?" })
  createdAt!: Date;

  @Field({ nullable: true, description: "When was this resource last updated?" })
  modifiedAt?: Date;
}
