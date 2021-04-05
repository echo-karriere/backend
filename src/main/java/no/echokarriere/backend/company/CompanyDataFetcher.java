package no.echokarriere.backend.company;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import no.echokarriere.graphql.types.Company;
import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.UUID;

@DgsComponent
@SuppressWarnings("unused") // Autowired by GraphQL
public class CompanyDataFetcher {
    private final CompanyService companyService;

    public CompanyDataFetcher(CompanyService companyService) {
        this.companyService = companyService;
    }

    @DgsQuery
    @Secured({"admin", "staff"})
    public List<Company> allCompanies() {
        return companyService.all();
    }

    @DgsQuery
    @Secured({"admin", "staff", "user"})
    public Company companyById(@InputArgument("id") UUID id) {
        return companyService.single(id);
    }

    @DgsMutation
    @Secured({"admin", "staff"})
    public Company createCompany(@InputArgument("input") CreateCompanyInput input) {
        return companyService.create(input);
    }

    @DgsMutation
    @Secured({"admin", "staff", "user"})
    public Company updateCompany(@InputArgument("id") UUID id, @InputArgument("input") UpdateCompanyInput input) {
        return companyService.update(id, input);
    }

    @DgsMutation
    @Secured({"admin", "staff", "user"})
    public boolean deleteCompany(@InputArgument("id") UUID id) {
        return companyService.delete(id);
    }
}
