import { Test, TestingModule } from "@nestjs/testing";

import { PrismaService } from "../prisma.service";
import { JobResolver } from "./job.resolver";
import { JobService } from "./job.service";

describe("JobResolver", () => {
  let resolver: JobResolver;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [JobResolver, JobService, PrismaService],
    }).compile();

    resolver = module.get<JobResolver>(JobResolver);
  });

  it("should be defined", () => {
    expect(resolver).toBeDefined();
  });
});
