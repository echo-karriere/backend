import { Field, ID, ObjectType } from "@nestjs/graphql";

@ObjectType({ description: "A subset of a user" })
export class User {
  @Field(() => ID, { description: "User ID" })
  id!: string;

  @Field(() => String, { description: "Name of the user" })
  name!: string;

  @Field(() => String, { description: "User email" })
  email!: string;

  @Field(() => Boolean, { description: "Is the account active?" })
  enabled!: boolean;

  @Field(() => [String], { description: "Users roles" })
  roles?: string[] | unknown[];
}
