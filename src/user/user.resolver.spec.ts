import { Test, TestingModule } from "@nestjs/testing";
import { UserResolver } from "./user.resolver";
import { AuthModule } from "../auth/auth.module";
import { PrismaModule } from "../prisma/prisma.module";

describe("UserResolver", () => {
  let resolver: UserResolver;

  beforeEach(async () => {
    process.env.JWT_TOKEN = "iamtesting";
    const module: TestingModule = await Test.createTestingModule({
      imports: [PrismaModule, AuthModule],
      providers: [UserResolver],
    }).compile();

    resolver = module.get<UserResolver>(UserResolver);
  });

  it("should be defined", () => {
    expect(resolver).toBeDefined();
  });
});
