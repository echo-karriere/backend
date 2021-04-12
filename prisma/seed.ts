import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

async function main() {
  if (!process.env.DEV_MODE) return;

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

    console.log({ categories, companies });
  } catch {
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
