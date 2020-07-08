/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
import { InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { Args, Context, Mutation, Resolver } from "@nestjs/graphql";
import argon2, { argon2id } from "argon2";
import { Request } from "express";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "../user/models/user.model";
import { AuthService } from "./auth.service";
import { loginDTO, passwordDTO } from "./dto";

@Resolver(() => User)
export class AuthResolver {
  constructor(private readonly authService: AuthService, private readonly prismaService: PrismaService) {}

  @Mutation(() => Boolean)
  async login(@Args() args: loginDTO, @Context() ctx: { req: Request }): Promise<boolean | Error> {
    return this.authService.login(args, ctx.req);
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
