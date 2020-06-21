import { ArgsType, Field, Int } from "@nestjs/graphql";

@ArgsType()
class ChangePasswordDTO {
  @Field((type) => Int)
  readonly id!: number;

  @Field()
  readonly password!: string;

  @Field()
  readonly newPassword!: string;
}

export default ChangePasswordDTO;
