import { Field, InputType } from "@nestjs/graphql";

@InputType()
export class UpdateCompanyInput {
  @Field()
  name: string;

  @Field()
  homepage: string;
}
