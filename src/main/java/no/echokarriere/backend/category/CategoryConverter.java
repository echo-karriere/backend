package no.echokarriere.backend.category;

import no.echokarriere.graphql.types.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<CategoryEntity, Category> {
    @Override
    public Category convert(CategoryEntity source) {
        return new Category(
                source.getId().toString(),
                source.getTitle(),
                source.getDescription(),
                source.getSlug(),
                source.getCreatedAt(),
                source.getModifiedAt()
        );
    }
}
