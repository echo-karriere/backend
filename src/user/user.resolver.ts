import { Args, Int, Mutation, Query, Resolver } from "@nestjs/graphql";
import argon2 from "argon2";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "./models/user.model";
import UserLogin from "./dto/login.dto";
import { UnauthorizedException } from "@nestjs/common";

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
  async login(@Args() args: UserLogin): Promise<User | UnauthorizedException> {
    const user = await this.prismaService.user.findOne({
      where: { email: args.email },
    });

    if (!user) throw new Error(`Unable to login`);
    try {
      if (await argon2.verify(user.password, args.password)) return user;
      else return new UnauthorizedException(`Unable to login`);
    } catch (err) {
      throw new Error(`Internal server error occurred`);
    }
  }
}
