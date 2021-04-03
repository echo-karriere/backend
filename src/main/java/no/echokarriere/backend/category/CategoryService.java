package no.echokarriere.backend.category;

import no.echokarriere.backend.exception.BadRequestException;
import no.echokarriere.backend.exception.ResourceNotFoundException;
import no.echokarriere.graphql.types.Category;
import no.echokarriere.graphql.types.CreateCategoryInput;
import no.echokarriere.graphql.types.UpdateCategoryInput;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ConversionService conversionService;

    public CategoryService(CategoryRepository categoryRepository, ConversionService conversionService) {
        this.categoryRepository = categoryRepository;
        this.conversionService = conversionService;
    }

    public List<Category> all() {
        return categoryRepository.selectAll()
                .stream()
                .map(it -> conversionService.convert(it, Category.class))
                .collect(Collectors.toList());
    }

    public Category single(UUID id) {
        return categoryRepository
                .select(id)
                .map(it -> conversionService.convert(it, Category.class))
                .orElseThrow(() -> new ResourceNotFoundException("No such category: " + id.toString()));
    }

    @Transactional
    public Category create(CreateCategoryInput input) {
        var entity = new CategoryEntity(input);
        return categoryRepository.create(entity)
                .map(it -> conversionService.convert(it, Category.class))
                .orElseThrow(() -> new BadRequestException("Could not create category"));
    }

    @Transactional
    public Category update(UUID id, UpdateCategoryInput input) {
        var entity = new CategoryEntity(id, input);
        return categoryRepository.update(entity)
                .map(it -> conversionService.convert(it, Category.class))
                .orElseThrow(() -> new BadRequestException("Could not update category"));
    }

    @Transactional
    public boolean delete(UUID id) {
        return categoryRepository.delete(id);
    }
}
