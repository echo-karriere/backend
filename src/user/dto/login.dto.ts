import { ArgsType, Field } from "@nestjs/graphql";

@ArgsType()
class UserLoginDTO {
  @Field()
  readonly email!: string;

  @Field()
  readonly password!: string;
}

export default UserLoginDTO;
