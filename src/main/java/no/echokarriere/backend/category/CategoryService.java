package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CreateCategoryDTO;
import no.echokarriere.backend.category.dto.UpdateCategoryDTO;
import no.echokarriere.backend.exception.NoSuchElementException;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new NoSuchElementException("No such category: " + id.toString()));
    }

    public Category create(CreateCategoryDTO categoryDTO) {
        var entity = new CategoryEntity(categoryDTO.getTitle(), categoryDTO.getDescription(), categoryDTO.getSlug());
        return categoryRepository.create(entity)
                .map(Category::new)
                .orElseThrow(() -> new RuntimeException("Could not create category"));
    }

    public Category update(UpdateCategoryDTO categoryDTO, UUID id) {
        var entity = new CategoryEntity(id, categoryDTO.getTitle(), categoryDTO.getDescription(), categoryDTO.getSlug());
        return categoryRepository.update(entity)
                .map(Category::new)
                .orElseThrow(() -> new RuntimeException("Could not update category"));
    }

    public void delete(UUID id) {
        categoryRepository.delete(id);
    }
}
