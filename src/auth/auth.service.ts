import { Injectable } from "@nestjs/common";
import argon2, { argon2id } from "argon2";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "../user/models/user.model";

@Injectable()
export class AuthService {
  constructor(private readonly prismaService: PrismaService) {}

  async validateUser(email: string, password: string): Promise<User | null> {
    const user = await this.prismaService.user.findOne({ where: { email } });
    if (user && (await argon2.verify(user.password, password, { type: argon2id }))) {
      return user;
    }

    return null;
  }
}
