import { InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { Args, Mutation, Resolver } from "@nestjs/graphql";
import argon2, { argon2id } from "argon2";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "../user/models/user.model";
import { AuthService } from "./auth.service";
import { loginDTO, passwordDTO } from "./dto";

@Resolver(() => User)
export class AuthResolver {
  constructor(private readonly authService: AuthService, private readonly prismaService: PrismaService) {}

  @Mutation(() => Boolean)
  async login(@Args() args: loginDTO): Promise<boolean | UnauthorizedException> {
    const user = await this.prismaService.user.findOne({
      where: { email: args.email },
    });

    if (!user) throw new UnauthorizedException(`Unable to login`);

    try {
      if (await this.authService.validateUser(args.email, args.password)) {
        return true;
      } else {
        return new UnauthorizedException(`Unable to login`);
      }
    } catch (err) {
      console.error(err);
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }

  @Mutation(() => User)
  async changePassword(@Args() args: passwordDTO): Promise<User | Error> {
    let user = await this.prismaService.user.findOne({ where: { id: args.id } });
    if (!user) throw new UnauthorizedException(`User not found`);

    try {
      if (await argon2.verify(user.password, args.password)) {
        user = await this.prismaService.user.update({
          where: { id: args.id },
          data: { password: await argon2.hash(args.newPassword, { version: argon2id }) },
        });

        return user;
      } else return new Error(`Password mismatch`);
    } catch (err) {
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }
}
