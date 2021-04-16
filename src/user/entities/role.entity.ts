import { Field, ID, ObjectType } from "@nestjs/graphql";

@ObjectType({ description: "A role assigned to users" })
export class Role {
  @Field(() => ID, { description: "Role ID" })
  id!: string;

  @Field(() => String, { description: "Name of the Role" })
  name!: string;

  @Field(() => String, { description: "Description of the role" })
  description!: string;
}
