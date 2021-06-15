import { JobType, PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

async function main() {
  try {
    const categories = await prisma.category.createMany({
      data: [
        {
          id: "af53312e-f87b-47e8-a14d-a63537ec7ddd",
          title: "Nyheter",
          description: "Sider med nyheter",
          slug: "news",
          createdAt: new Date(),
        },
        {
          id: "daf2fe8a-54c8-4d02-b201-ac1bf121b0ac",
          title: "For studenter",
          description: "Sider for studenter",
          slug: "for-studenter",
          createdAt: new Date(),
        },
        {
          id: "bdddaaea-e8e0-42cc-aaf4-87014163a43f",
          title: "For bedrifter",
          description: "Sider for bedrifter",
          slug: "for-bedrifter",
          createdAt: new Date(),
        },
      ],
    });

    const companies = await prisma.company.createMany({
      data: [
        {
          id: "a04e620b-b9b9-4012-a202-83c15c96fc77",
          name: "Acme Corporation",
          homepage: "acme.com",
          createdAt: new Date(),
        },
        {
          id: "40468aaa-e5ae-4d51-b0d2-931842d2702f",
          name: "Globex Corporation",
          homepage: "globex.org",
          createdAt: new Date(),
        },
        {
          id: "337e1370-cb5c-45b6-ad6c-fdd925a27313",
          name: "Umbrella Corporation",
          homepage: "umbrella.com",
          createdAt: new Date(),
        },
      ],
    });

    const today = new Date();

    const jobs = await prisma.job.createMany({
      data: [
        {
          id: "e0899f1f-3c28-4468-b7a1-bbadaeca4e49",
          title: "Internship",
          description: "Get a cool internship",
          type: JobType.SUMMER,
          url: "https://www.example.org",
          published: true,
          location: "Oslo",
          companyId: "337e1370-cb5c-45b6-ad6c-fdd925a27313",
          deadline: new Date(today.setMonth(today.getMonth() + 2)),
          finalExpiration: new Date(today.setMonth(today.getMonth() + 3)),
        },
        {
          id: "4501eb53-3502-472f-9a10-8c8dce2b7645",
          title: "Part time junior developer",
          description: "Mentoring not included",
          type: JobType.PART,
          url: "https://www.test.org/junior",
          published: true,
          location: "Oslo",
          companyId: "337e1370-cb5c-45b6-ad6c-fdd925a27313",
          deadline: new Date(today.setMonth(today.getMonth() + 4)),
          finalExpiration: new Date(today.setMonth(today.getMonth() + 6)),
        },
        {
          id: "1bd9b7fa-1285-4a19-bf1e-0913ce915b8c",
          title: "Full-stack developer",
          description: "Write some hot COBOL",
          type: JobType.FULL,
          url: "https://www.test.org/full",
          published: true,
          location: "Bergen",
          companyId: "a04e620b-b9b9-4012-a202-83c15c96fc77",
        },
      ],
    });
    console.dir({ categories, companies, jobs });
  } catch (error) {
    console.error(error);
    return;
  }
}

main()
  .catch((error) => {
    console.error(error);
    // eslint-disable-next-line no-process-exit, unicorn/no-process-exit
    process.exit(1);
  })
  .finally(() => {
    void prisma.$disconnect();
  });
