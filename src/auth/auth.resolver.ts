import { UnauthorizedException, InternalServerErrorException, UseGuards } from "@nestjs/common";
import { passwordDTO, loginDTO } from "./dto";
import argon2, { argon2id } from "argon2";
import { User } from "../user/models/user.model";
import { Mutation, Args, Resolver } from "@nestjs/graphql";
import { AuthService } from "./auth.service";
import { PrismaService } from "../prisma/prisma.service";
import { JwtToken } from "./models/jwt.model";
import { GqlAuthGuard } from "../decorators/gql-auth-guard";
import { CurrentUser } from "../decorators/current-user";

@Resolver(() => User)
export class AuthResolver {
  constructor(private readonly authService: AuthService, private readonly prismaService: PrismaService) {}

  @Mutation(() => JwtToken)
  async login(@Args() args: loginDTO): Promise<JwtToken | UnauthorizedException> {
    const user = await this.prismaService.user.findOne({
      where: { email: args.email },
    });

    if (!user) throw new UnauthorizedException(`Unable to login`);

    try {
      if (await argon2.verify(user.password, args.password)) {
        return await this.authService.generateToken({ id: user.id, email: user.email });
      } else {
        return new UnauthorizedException(`Unable to login`);
      }
    } catch (err) {
      console.error(err);
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }

  @Mutation(() => User)
  @UseGuards(GqlAuthGuard)
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

  @Mutation(() => JwtToken)
  @UseGuards(GqlAuthGuard)
  async refreshToken(@Args("token") token: string, @CurrentUser() user: User): Promise<JwtToken> {
    return this.authService.generateToken({ id: user.id, email: user.email });
  }
}
