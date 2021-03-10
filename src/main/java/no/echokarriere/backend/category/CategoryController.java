package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CreateCategoryDTO;
import no.echokarriere.backend.category.dto.UpdateCategoryDTO;
import no.echokarriere.backend.category.errors.CategoryCreationException;
import no.echokarriere.backend.category.errors.CategoryNotFoundException;
import no.echokarriere.backend.category.errors.CategoryUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    List<Category> all() {
        return categoryRepository.selectAll().stream().map(Category::new).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    Category one(@PathVariable UUID id) {
        return categoryRepository
                .select(id)
                .map(Category::new)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PostMapping
    Category create(@RequestBody CreateCategoryDTO categoryDTO) {
        var entity = new CategoryEntity(categoryDTO.title(), categoryDTO.description(), categoryDTO.slug());
        return categoryRepository
                .create(entity)
                .map(Category::new)
                .orElseThrow(CategoryCreationException::new);
    }

    @PutMapping("{id}")
    Category update(@RequestBody UpdateCategoryDTO categoryDTO, @PathVariable UUID id) {
        var entity = new CategoryEntity(id, categoryDTO.title(), categoryDTO.description(), categoryDTO.slug());
        return categoryRepository
                .update(entity)
                .map(Category::new)
                .orElseThrow(() -> new CategoryUpdateException(id));
    }

    @DeleteMapping("{id}")
    boolean delete(@PathVariable UUID id) {
        return categoryRepository.delete(id);
    }
}
