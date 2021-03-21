package no.echokarriere.backend.company;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import no.echokarriere.graphql.DgsConstants;
import no.echokarriere.graphql.types.Company;
import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;

import java.util.List;
import java.util.UUID;

@DgsComponent
@SuppressWarnings("unused") // Autowired by GraphQL
public class CompanyDataFetcher {
    private final CompanyService companyService;

    public CompanyDataFetcher(CompanyService companyService) {
        this.companyService = companyService;
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.AllCompanies)
    public List<Company> allCompanies() {
        return companyService.all();
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.CompanyById)
    public Company fromId(@InputArgument("id") UUID id) {
        return companyService.one(id);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateCompany)
    public Company createCompany(@InputArgument("input") CreateCompanyInput input) {
        return companyService.create(input);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.UpdateCompany)
    public Company updateCompany(@InputArgument("id") UUID id, @InputArgument("input") UpdateCompanyInput input) {
        return companyService.update(id, input);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.DeleteCompany)
    public boolean deleteCompany(@InputArgument("id") UUID id) {
        return companyService.delete(id);
    }
}
