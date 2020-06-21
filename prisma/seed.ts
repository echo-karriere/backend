/* eslint-disable @typescript-eslint/no-misused-promises */
import { PrismaClient } from "@prisma/client";
import argon2, { argon2id } from "argon2";

const prisma = new PrismaClient();

async function main(): Promise<void> {
  const user1 = await prisma.user.create({
    data: {
      email: "test@example.org",
      password: await argon2.hash("password123", {
        type: argon2id,
      }),
      name: "Test Testerson",
      staff: true,
      admin: true,
      active: true,
    },
  });

  const user2 = await prisma.user.create({
    data: {
      email: "ola@nordmann.org",
      password: await argon2.hash("helloworld", { type: argon2id }),
      name: "Ola Nordmann",
      staff: true,
      admin: false,
      active: true,
    },
  });

  console.log(user1, user2);
}

main()
  .catch((e) => console.error(e))
  .finally(async () => {
    await prisma.disconnect();
  });
