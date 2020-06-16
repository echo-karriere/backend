/* eslint-disable @typescript-eslint/no-misused-promises */
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

async function main(): Promise<void> {
  const user1 = await prisma.user.create({
    data: {
      email: "test@example.org",
      name: "Test Testerson",
    },
  });

  const user2 = await prisma.user.create({
    data: {
      email: "ola@nordmann.org",
      name: "Ola Nordmann",
    },
  });

  console.log(user1, user2);
}

main()
  .catch(e => console.error(e))
  .finally(async () => {
    await prisma.disconnect();
  });
