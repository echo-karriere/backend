import { Field, ID, ObjectType } from "@nestjs/graphql";

@ObjectType({ description: "A role assigned to users" })
export class Role {
  @Field(() => ID, { description: "The role ID" })
  id!: string;

  @Field({ description: "Name of the role" })
  name!: string;

  @Field({ description: "Role description" })
  description!: string;
}
