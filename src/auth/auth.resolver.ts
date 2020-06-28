import { UnauthorizedException, InternalServerErrorException } from "@nestjs/common";
import { passwordDTO, loginDTO } from "./dto";
import argon2, { argon2id } from "argon2";
import { User } from "../user/models/user.model";
import { Mutation, Args, Resolver } from "@nestjs/graphql";
import { AuthService } from "./auth.service";
import { PrismaService } from "../prisma/prisma.service";
import { GqlRes } from "./auth.decorators";
import { Response } from "express";

@Resolver(() => User)
export class AuthResolver {
  constructor(private readonly authService: AuthService, private readonly prismaService: PrismaService) {}

  @Mutation((returns) => User)
  async login(@Args() args: loginDTO, @GqlRes() res: Response): Promise<User | UnauthorizedException> {
    const user = await this.prismaService.user.findOne({
      where: { email: args.email },
    });

    if (!user) throw new UnauthorizedException(`Unable to login`);

    try {
      if (await argon2.verify(user.password, args.password)) {
        const { accessToken, refreshToken } = await this.authService.generateToken({ id: user.id, email: user.email });
        res.cookie("access_token", accessToken, { httpOnly: true });
        res.cookie("refresh_token", refreshToken, { httpOnly: true });

        return user;
      } else {
        return new UnauthorizedException(`Unable to login`);
      }
    } catch (err) {
      console.error(err);
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }

  @Mutation((returns) => User)
  async changePassword(@Args() args: passwordDTO): Promise<{ accessToken: string; refreshToken: string } | Error> {
    let user = await this.prismaService.user.findOne({ where: { id: args.id } });
    if (!user) throw new UnauthorizedException(`User not found`);

    try {
      if (await argon2.verify(user.password, args.password)) {
        user = await this.prismaService.user.update({
          where: { id: args.id },
          data: { password: await argon2.hash(args.newPassword, { version: argon2id }) },
        });

        return this.authService.generateToken({ id: user.id, email: user.email });
      } else return new Error(`Password mismatch`);
    } catch (err) {
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }
}
