import { ConfigModule } from "@nestjs/config";
import { PassportModule } from "@nestjs/passport";
import { Test, TestingModule } from "@nestjs/testing";
import { PrismaModule } from "../prisma/prisma.module";
import { AuthService } from "./auth.service";

describe("AuthService", () => {
  let service: AuthService;

  beforeEach(async () => {
    process.env.JWT_TOKEN = "iamtesting";
    const module: TestingModule = await Test.createTestingModule({
      imports: [PassportModule, ConfigModule, PrismaModule],
      providers: [AuthService],
    }).compile();

    service = module.get<AuthService>(AuthService);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
