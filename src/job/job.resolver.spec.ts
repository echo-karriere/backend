import { Test, TestingModule } from "@nestjs/testing";

import { JobResolver } from "./job.resolver";
import { JobService } from "./job.service";

describe("JobResolver", () => {
  let resolver: JobResolver;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [JobResolver, JobService],
    }).compile();

    resolver = module.get<JobResolver>(JobResolver);
  });

  it("should be defined", () => {
    expect(resolver).toBeDefined();
  });
});
