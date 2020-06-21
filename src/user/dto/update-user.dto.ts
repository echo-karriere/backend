import { ArgsType, Field, Int } from "@nestjs/graphql";

@ArgsType()
class UpdateUserDTO {
  @Field((type) => Int)
  readonly id!: number;

  @Field({ nullable: true })
  readonly email?: string;

  @Field({ nullable: true })
  readonly name?: string;

  @Field((type) => String, { nullable: true })
  readonly avatar?: string | null;

  @Field({ nullable: true })
  readonly staff?: boolean;

  @Field({ nullable: true })
  readonly admin?: boolean;

  @Field({ nullable: true })
  readonly active?: boolean;
}

export default UpdateUserDTO;
