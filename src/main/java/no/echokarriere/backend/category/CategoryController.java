package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CreateCategoryDTO;
import no.echokarriere.backend.category.dto.UpdateCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    @PostMapping
    Category create(@RequestBody CreateCategoryDTO categoryDTO) {
        var entity = new CategoryEntity(categoryDTO.title(), categoryDTO.description(), categoryDTO.slug());
        return categoryRepository
                .create(entity)
                .map(Category::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create category"));
    }

    @PutMapping("{id}")
    Category update(@RequestBody UpdateCategoryDTO categoryDTO, @PathVariable UUID id) {
        var entity = new CategoryEntity(id, categoryDTO.title(), categoryDTO.description(), categoryDTO.slug());
        return categoryRepository
                .update(entity)
                .map(Category::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update category"));
    }

    @DeleteMapping("{id}")
    boolean delete(@PathVariable UUID id) {
        return categoryRepository.delete(id);
    }
}
