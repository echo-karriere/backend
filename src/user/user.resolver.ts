import { Args, Int, Mutation, Query, Resolver } from "@nestjs/graphql";
import { UnauthorizedException, UseGuards } from "@nestjs/common";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "./models/user.model";
import UpdateUserDTO from "./dto/update-user.dto";
import { CurrentUser } from "../decorators/current-user";
import { GqlAuthGuard } from "../decorators/gql-auth-guard";

@Resolver(() => User)
export class UserResolver {
  constructor(private readonly prismaService: PrismaService) {}

  @Query(() => User)
  @UseGuards(GqlAuthGuard)
  async me(@CurrentUser() user: User): Promise<User | null> {
    return this.prismaService.user.findOne({ where: { id: user.id } });
  }

  @Query(() => User)
  @UseGuards(GqlAuthGuard)
  async user(@Args("id", { type: () => Int }) id: number): Promise<User | null> {
    return this.prismaService.user.findOne({ where: { id } });
  }

  @Query(() => [User])
  @UseGuards(GqlAuthGuard)
  async users(): Promise<User[]> {
    return this.prismaService.user.findMany();
  }

  @Mutation(() => User)
  @UseGuards(GqlAuthGuard)
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
