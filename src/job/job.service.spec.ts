import { Test, TestingModule } from "@nestjs/testing";

import { PrismaService } from "../prisma.service";
import { JobService } from "./job.service";

describe("JobService", () => {
  let service: JobService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [JobService, PrismaService],
    }).compile();

    service = module.get<JobService>(JobService);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
