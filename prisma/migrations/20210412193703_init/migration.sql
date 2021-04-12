-- CreateTable
CREATE TABLE "Category" (
    "id" UUID NOT NULL,
    "title" TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "slug" TEXT NOT NULL,
    "createdAt" TIMESTAMPTZ(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "modifiedAt" TIMESTAMPTZ(6),

    PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Company" (
    "id" UUID NOT NULL,
    "name" TEXT NOT NULL,
    "homepage" TEXT,
    "createdAt" TIMESTAMPTZ(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "modifiedAt" TIMESTAMPTZ(6),

    PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "Category.title_unique" ON "Category"("title");

-- CreateIndex
CREATE UNIQUE INDEX "Category.slug_unique" ON "Category"("slug");

-- CreateIndex
CREATE UNIQUE INDEX "Company.name_unique" ON "Company"("name");
