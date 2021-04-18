import { Field, ID, InputType } from "@nestjs/graphql";

@InputType()
export class UpdateUserInput {
  @Field({ nullable: true })
  name?: string;

  @Field({ nullable: true })
  email?: string;

  @Field({ nullable: true })
  enabled?: boolean;

  @Field(() => [ID], { nullable: true })
  roles?: string[];
}
