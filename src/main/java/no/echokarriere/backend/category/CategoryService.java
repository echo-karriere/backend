package no.echokarriere.backend.category;

import no.echokarriere.backend.configuration.CrudService;
import no.echokarriere.backend.exception.BadRequestException;
import no.echokarriere.backend.exception.NoSuchElementException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService implements CrudService<Category, CategoryDTO, UUID> {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> all() {
        return categoryRepository.selectAll().stream().map(Category::new).collect(Collectors.toList());
    }

    public Category single(UUID id) {
        return categoryRepository
                .select(id)
                .map(Category::new)
                .orElseThrow(() -> new NoSuchElementException("No such category: " + id.toString()));
    }

    public Category create(CategoryDTO categoryDTO) {
        var entity = new CategoryEntity(categoryDTO);
        return categoryRepository.create(entity)
                .map(Category::new)
                .orElseThrow(() -> new BadRequestException("Could not create category"));
    }

    public Category update(CategoryDTO categoryDTO, UUID id) {
        var entity = new CategoryEntity(id, categoryDTO);
        return categoryRepository.update(entity)
                .map(Category::new)
                .orElseThrow(() -> new BadRequestException("Could not update category"));
    }

    public boolean delete(UUID id) {
        return categoryRepository.delete(id);
    }
}
