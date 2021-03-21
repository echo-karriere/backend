package no.echokarriere.backend.category;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import no.echokarriere.graphql.DgsConstants;
import no.echokarriere.graphql.types.Category;
import no.echokarriere.graphql.types.CreateCategoryInput;
import no.echokarriere.graphql.types.UpdateCategoryInput;

import java.util.List;
import java.util.UUID;

@DgsComponent
@SuppressWarnings("unused") // Autowired by GraphQL
public class CategoryDataFetcher {
    private final CategoryService categoryService;

    public CategoryDataFetcher(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.AllCategories)
    public List<Category> allCompanies() {
        return categoryService.all();
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.CategoryById)
    public Category fromId(@InputArgument("id") UUID id) {
        return categoryService.single(id);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateCategory)
    public Category createCategory(@InputArgument("input") CreateCategoryInput input) {
        return categoryService.create(input);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.UpdateCategory)
    public Category updateCategory(@InputArgument("id") UUID id, @InputArgument("input") UpdateCategoryInput input) {
        return categoryService.update(id, input);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.DeleteCategory)
    public boolean deleteCategory(@InputArgument("id") UUID id) {
        return categoryService.delete(id);
    }
}
