import { Field, InputType } from "@nestjs/graphql";
import { IsOptional } from "class-validator";

@InputType()
export class UpdateCategoryInput {
  @Field()
  title: string;

  @Field({ nullable: true })
  @IsOptional()
  description?: string;

  @Field()
  slug: string;
}
