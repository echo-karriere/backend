package no.echokarriere.backend.company;

import no.echokarriere.graphql.types.Company;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyConverter implements Converter<CompanyEntity, Company> {
    @Override
    public Company convert(CompanyEntity source) {
        return new Company(
                source.getId().toString(),
                source.getName(),
                source.getHomepage(),
                source.getCreatedAt(),
                source.getModifiedAt()
        );
    }
}
