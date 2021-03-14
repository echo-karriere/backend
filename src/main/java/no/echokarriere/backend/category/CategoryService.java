package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CreateCategoryDTO;
import no.echokarriere.backend.category.dto.UpdateCategoryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> all() {
        return categoryRepository.findAll();
    }

    public Category one(UUID id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public Category create(CreateCategoryDTO categoryDTO) {
        var entity = new Category(categoryDTO.getTitle(), categoryDTO.getDescription(), categoryDTO.getSlug());
        return categoryRepository.save(entity);
    }

    public Category update(UpdateCategoryDTO categoryDTO, UUID id) {
        var entity = new Category(categoryDTO.getTitle(), categoryDTO.getDescription(), categoryDTO.getSlug());
        entity.setId(id);
        return categoryRepository.save(entity);
    }

    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}
