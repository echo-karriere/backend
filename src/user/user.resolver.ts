import { Args, Int, Mutation, Query, Resolver } from "@nestjs/graphql";
import { InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import argon2, { argon2id } from "argon2";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "./models/user.model";
import { ChangePasswordDTO, UserLoginDTO } from "./dto";
import UpdateUserDTO from "./dto/update-user.dto";

@Resolver((of: void) => User)
export class UserResolver {
  constructor(private prismaService: PrismaService) {}

  @Query((returns) => User)
  async user(@Args("id", { type: () => Int }) id: number): Promise<User | null> {
    return this.prismaService.user.findOne({ where: { id } });
  }

  @Query((returns) => [User])
  async users(): Promise<User[]> {
    return this.prismaService.user.findMany();
  }

  @Mutation((returns) => User)
  async login(@Args() args: UserLoginDTO): Promise<User | UnauthorizedException> {
    const user = await this.prismaService.user.findOne({
      where: { email: args.email },
    });

    if (!user) throw new UnauthorizedException(`Unable to login`);
    try {
      if (await argon2.verify(user.password, args.password)) return user;
      else return new UnauthorizedException(`Unable to login`);
    } catch (err) {
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }

  @Mutation((returns) => User)
  async changePassword(@Args() args: ChangePasswordDTO): Promise<User | Error> {
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

  @Mutation((returns) => User)
  async change(@Args() args: UpdateUserDTO): Promise<User> {
    let user = await this.prismaService.user.findOne({ where: { id: args.id } });
    if (!user) throw new UnauthorizedException(`User not found`);

    user = await this.prismaService.user.update({
      where: { id: args.id },
      data: {
        email: args.email,
        name: args.name,
        avatar: args.avatar,
        staff: args.staff,
        admin: args.admin,
        active: args.active,
      },
    });

    return user;
  }
}
