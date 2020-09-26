package no.echokarriere.category

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.echokarriere.utils.DatabaseDescribeSpec
import java.util.UUID

class CategoryRepositorySpec : DatabaseDescribeSpec({ body() })

private val body: DescribeSpec.() -> Unit = {
    val categoryRepository = CategoryRepository()
    val categoryId = UUID.randomUUID()

    describe("CategoryRepository") {
        it("can create a new category") {
            val title = "Test Category"
            val description = "A test category"
            val slug = "test-category"

            val category = CategoryEntity.create(
                id = categoryId,
                title,
                description,
                slug
            )

            val created = categoryRepository.insert(category)

            created shouldNotBe null
            created?.id shouldBe categoryId
            created?.title shouldBe title
            created?.description shouldBe description
            created?.slug shouldBe slug
        }

        it("can find created entity") {
            val found = categoryRepository.select(categoryId)

            found shouldNotBe null
            found?.id shouldBe categoryId
        }

        it("contains created entity") {
            val all = categoryRepository.selectAll()
            val found = all.find { it.id == categoryId }

            found shouldNotBe null
            found?.id shouldBe categoryId
        }

        it("can update an entity") {
            val newTitle = "Better Category"
            val newDescription = "The bestest category"
            val newSlug = "the-best"

            val old = categoryRepository.select(categoryId)!!

            val new = old.updateDetails(
                title = newTitle,
                description = newDescription,
                slug = newSlug
            )

            val updated = categoryRepository.update(new)

            updated shouldNotBe null
            updated?.id shouldBe categoryId
            updated?.title shouldBe newTitle
            updated?.description shouldBe newDescription
            updated?.slug shouldBe newSlug
        }

        it("can delete an entity") {
            val deleted = categoryRepository.delete(categoryId)

            deleted shouldBe true

            val doubleDeleted = categoryRepository.delete(categoryId)
            doubleDeleted shouldBe false

            val itIsGone = categoryRepository.select(categoryId)
            itIsGone shouldBe null
        }
    }
}
