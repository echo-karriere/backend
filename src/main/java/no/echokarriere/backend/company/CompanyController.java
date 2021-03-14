package no.echokarriere.backend.company;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.echokarriere.backend.company.dto.CreateCompanyDTO;
import no.echokarriere.backend.company.dto.UpdateCompanyDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/companies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @Operation(summary = "Get all companies", tags = {"companies"})
    public List<Company> all() {
        return companyService.all();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get information about a single company", tags = {"companies"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Company.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    public Company one(
            @Parameter(description = "ID of company") @PathVariable UUID id
    ) {
        return companyService.one(id);
    }

    @PostMapping
    @Operation(summary = "Create a new company", tags = {"companies"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Company.class))),
            @ApiResponse(responseCode = "400", description = "Could not create a new company", content = @Content)
    })
    public Company create(
            @Parameter(description = "Data used to create a new company") @RequestBody CreateCompanyDTO categoryDTO
    ) {
        return companyService.create(categoryDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update an existing company", tags = {"companies"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Company.class))),
            @ApiResponse(responseCode = "400", description = "Could not update company, bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Could not find company", content = @Content)
    })
    public Company update(
            @Parameter(description = "Data used to update a company") @RequestBody UpdateCompanyDTO categoryDTO,
            @Parameter(description = "ID of company") @PathVariable UUID id
    ) {
        return companyService.update(categoryDTO, id);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a company from the service", tags = {"companies"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    public boolean delete(
            @Parameter(description = "ID of company") @PathVariable UUID id
    ) {
        return companyService.delete(id);
    }
}
