import { Resolver, Args, Query, Int } from "@nestjs/graphql";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "./models/user.model";

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
}
