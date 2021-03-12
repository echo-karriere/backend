package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CreateCategoryDTO;
import no.echokarriere.backend.category.dto.UpdateCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> all() {
        return categoryService.all();
    }

    @GetMapping("{id}")
    public Category one(@PathVariable UUID id) {
        return categoryService.one(id);
    }

    @PostMapping
    public Category create(@RequestBody CreateCategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @PutMapping("{id}")
    public Category update(@RequestBody UpdateCategoryDTO categoryDTO, @PathVariable UUID id) {
        return categoryService.update(categoryDTO, id);
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable UUID id) {
        return categoryService.delete(id);
    }
}
