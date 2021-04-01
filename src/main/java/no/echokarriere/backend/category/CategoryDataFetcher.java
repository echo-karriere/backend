package no.echokarriere.backend.category;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
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

    @DgsQuery
    public List<Category> allCategories() {
        return categoryService.all();
    }

    @DgsQuery
    public Category categoryById(@InputArgument("id") UUID id) {
        return categoryService.single(id);
    }

    @DgsMutation
    public Category createCategory(@InputArgument("input") CreateCategoryInput input) {
        return categoryService.create(input);
    }

    @DgsMutation
    public Category updateCategory(@InputArgument("id") UUID id, @InputArgument("input") UpdateCategoryInput input) {
        return categoryService.update(id, input);
    }

    @DgsMutation
    public boolean deleteCategory(@InputArgument("id") UUID id) {
        return categoryService.delete(id);
    }
}
