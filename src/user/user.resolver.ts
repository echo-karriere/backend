import { UnauthorizedException } from "@nestjs/common";
import { Args, Int, Mutation, Query, Resolver } from "@nestjs/graphql";
import { CurrentUser } from "../decorators/current-user";
import { PrismaService } from "../prisma/prisma.service";
import UpdateUserDTO from "./dto/update-user.dto";
import { User } from "./models/user.model";

@Resolver(() => User)
export class UserResolver {
  constructor(private readonly prismaService: PrismaService) {}

  @Query(() => User)
  async me(@CurrentUser() user: User): Promise<User | null> {
    return this.prismaService.user.findOne({ where: { id: user.id } });
  }

  @Query(() => User)
  async user(@Args("id", { type: () => Int }) id: number): Promise<User | null> {
    return this.prismaService.user.findOne({ where: { id } });
  }

  @Query(() => [User])
  async users(): Promise<User[]> {
    return this.prismaService.user.findMany();
  }

  @Mutation(() => User)
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
