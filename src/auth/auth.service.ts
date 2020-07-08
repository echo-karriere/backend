import { Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import argon2, { argon2id } from "argon2";
import { Request } from "express";
import { PrismaService } from "../prisma/prisma.service";
import { User } from "../user/models/user.model";
import { loginDTO } from "./dto";

@Injectable()
export class AuthService {
  constructor(private readonly prismaService: PrismaService) {}

  // async validateSession(req: Request): Promise<boolean | null> {
  validateSession(req: Request): boolean | null {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call, @typescript-eslint/no-unsafe-member-access
    if (req.body.includes("IntrospectionQuery")) return null;
    if (!req.session?.sessionID) return null;

    return null;
  }

  async validateUser(email: string, password: string): Promise<User | null> {
    const user = await this.prismaService.user.findOne({ where: { email } });
    if (user && (await argon2.verify(user.password, password, { type: argon2id }))) {
      return user;
    }

    return null;
  }

  async login(input: loginDTO, req: Request): Promise<boolean | Error> {
    try {
      if (await this.validateUser(input.email, input.password)) {
        if (req.session) req.session.userId = "wat";
        return true;
      } else {
        return new UnauthorizedException(`Unable to login`);
      }
    } catch (err) {
      console.error(err);
      throw new InternalServerErrorException(`Internal server error occurred`);
    }
  }
}
