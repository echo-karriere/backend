package no.echokarriere.backend.category;

import no.echokarriere.backend.category.dto.CategoryDTO;
import no.echokarriere.backend.exception.NoSuchElementException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> all() {
        var categories = categoryRepository.findAll();
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CategoryDTO one(UUID id) {
        return categoryRepository
                .findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NoSuchElementException("No such category: " + id.toString()));
    }

    public CategoryDTO create(CategoryDTO categoryDTO) {
        var entity = fromDTO(categoryDTO);
        return toDTO(categoryRepository.save(entity));
    }

    public Category update(CategoryDTO categoryDTO, UUID id) {
        var category = categoryRepository.findById(id);
        return categoryRepository.save(entity);
    }

    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }

    private Category fromDTO(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }

    private CategoryDTO toDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}
