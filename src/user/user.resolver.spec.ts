import { Test, TestingModule } from "@nestjs/testing";
import { UserResolver } from "./user.resolver";

describe("UserResolver", () => {
  let service: UserResolver;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [UserResolver],
    }).compile();

    service = module.get<UserResolver>(UserResolver);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
