import { Field, ArgsType } from "@nestjs/graphql";

@ArgsType()
class UserLogin {
  @Field()
  readonly email!: string;
  @Field()
  readonly password!: string;
}

export default UserLogin;
