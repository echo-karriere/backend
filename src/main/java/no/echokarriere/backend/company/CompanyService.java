package no.echokarriere.backend.company;

import no.echokarriere.backend.company.dto.CreateCompanyDTO;
import no.echokarriere.backend.company.dto.UpdateCompanyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> all() {
        return companyRepository.selectAll().stream().map(Company::new).collect(Collectors.toList());
    }

    public Company one(UUID id) {
        return companyRepository
                .select(id)
                .map(Company::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public Company create(CreateCompanyDTO categoryDTO) {
        var entity = new CompanyEntity(categoryDTO);
        return companyRepository
                .create(entity)
                .map(Company::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create category"));
    }

    public Company update(UpdateCompanyDTO categoryDTO, UUID id) {
        var entity = new CompanyEntity(id, categoryDTO);
        return companyRepository
                .update(entity)
                .map(Company::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update category"));
    }

    public boolean delete(UUID id) {
        return companyRepository.delete(id);
    }
}
