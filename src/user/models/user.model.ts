import { Field, Int, ObjectType } from "@nestjs/graphql";

@ObjectType()
export class User {
  @Field((type) => Int)
  id!: number;

  @Field()
  email!: string;

  @Field()
  password!: string;

  @Field()
  name!: string;

  @Field((type) => String, { nullable: true })
  avatar?: string | null;

  @Field()
  staff!: boolean;

  @Field()
  admin!: boolean;

  @Field()
  active!: boolean;
}
