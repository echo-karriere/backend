import { Field, InputType } from "@nestjs/graphql";

@InputType()
export class CreateCompanyInput {
  @Field()
  name!: string;

  @Field()
  homepage!: string;
}
