package no.echokarriere.backend.company;

import no.echokarriere.backend.company.dto.CreateCompanyDTO;
import no.echokarriere.backend.company.dto.UpdateCompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/companies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> all() {
        return companyService.all();
    }

    @GetMapping("{id}")
    public Company one(@PathVariable UUID id) {
        return companyService.one(id);
    }

    @PostMapping
    public Company create(@RequestBody CreateCompanyDTO categoryDTO) {
        return companyService.create(categoryDTO);
    }

    @PutMapping("{id}")
    public Company update(@RequestBody UpdateCompanyDTO categoryDTO, @PathVariable UUID id) {
        return companyService.update(categoryDTO, id);
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable UUID id) {
        return companyService.delete(id);
    }
}
