import { Field, ID, ObjectType } from "@nestjs/graphql";

@ObjectType({
  description: "A company that is or has participated in one of our events.",
})
export class Company {
  @Field(() => ID, { description: "Company ID" })
  id!: string;

  @Field({ description: "Name of company" })
  name!: string;

  @Field({ description: "A link to their homepage" })
  homepage!: string;

  @Field({ description: "When was this resource created?" })
  createdAt!: Date;

  @Field({ nullable: true, description: "When was this resource last updated?" })
  modifiedAt?: Date | null;
}
