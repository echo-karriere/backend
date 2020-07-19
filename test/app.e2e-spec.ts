import { Test, TestingModule } from "@nestjs/testing";
import { INestApplication } from "@nestjs/common";
import request from "supertest";
import { AppModule } from "../src/app.module";
import { FastifyAdapter, NestFastifyApplication } from "@nestjs/platform-fastify";

describe("AppController (e2e)", () => {
  let app: INestApplication;

  beforeEach(async () => {
    const moduleFixture: TestingModule = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    app = moduleFixture.createNestApplication<NestFastifyApplication>(new FastifyAdapter());
    await app.init();
    app.getHttpAdapter().getInstance().ready();
  });

  afterAll(() => {
    app.close();
  });

  it("/ (GET)", () => {
    return request(app.getHttpServer()).get("/").expect(200).expect("Hello World!");
  });
});
