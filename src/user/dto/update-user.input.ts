import { Field, InputType } from "@nestjs/graphql";

@InputType()
export class UpdateUserInput {
  @Field()
  name!: string;

  @Field()
  email!: string;
}
