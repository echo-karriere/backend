package no.echokarriere.backend.company;

import no.echokarriere.graphql.types.Company;
import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ConversionService conversionService;

    public CompanyService(CompanyRepository companyRepository, ConversionService conversionService) {
        this.companyRepository = companyRepository;
        this.conversionService = conversionService;
    }

    public List<Company> all() {
        return companyRepository.selectAll()
                .stream()
                .map(it -> conversionService.convert(it, Company.class))
                .collect(Collectors.toList());
    }

    public Company one(UUID id) {
        return companyRepository
                .select(id)
                .map(it -> conversionService.convert(it, Company.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public Company create(CreateCompanyInput input) {
        var entity = new CompanyEntity(input);
        return companyRepository
                .create(entity)
                .map(it -> conversionService.convert(it, Company.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create category"));
    }

    public Company update(UUID id, UpdateCompanyInput input) {
        var entity = new CompanyEntity(id, input);
        return companyRepository
                .update(entity)
                .map(it -> conversionService.convert(it, Company.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update category"));
    }

    public boolean delete(UUID id) {
        return companyRepository.delete(id);
    }
}
