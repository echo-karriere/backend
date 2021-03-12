package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CreateCategoryDTO;
import no.echokarriere.backend.category.dto.UpdateCategoryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> all() {
        return categoryRepository.selectAll().stream().map(Category::new).collect(Collectors.toList());
    }

    public Category one(UUID id) {
        return categoryRepository
                .select(id)
                .map(Category::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public Category create(CreateCategoryDTO categoryDTO) {
        var entity = new CategoryEntity(categoryDTO.getTitle(), categoryDTO.getDescription(), categoryDTO.getSlug());
        return categoryRepository
                .create(entity)
                .map(Category::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create category"));
    }

    public Category update(UpdateCategoryDTO categoryDTO, UUID id) {
        var entity = new CategoryEntity(id, categoryDTO.getTitle(), categoryDTO.getDescription(), categoryDTO.getSlug());
        return categoryRepository
                .update(entity)
                .map(Category::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update category"));
    }

    public boolean delete(UUID id) {
        return categoryRepository.delete(id);
    }
}
