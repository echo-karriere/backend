package no.echokarriere.backend.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories", tags = {"categories"})
    public List<Category> all() {
        return categoryService.all();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get information about a single category", tags = {"categories"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    public Category one(
            @Parameter(description = "ID of category") @PathVariable UUID id
    ) {
        return categoryService.single(id);
    }

    @PostMapping
    @Operation(summary = "Create a new category", tags = {"categories"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Could not create a new category", content = @Content)
    })
    public Category create(
            @Parameter(description = "Data used to create a new category") @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        return categoryService.create(categoryDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update an existing category", tags = {"categories"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Could not update category, bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Could not find category", content = @Content)
    })
    public Category update(
            @Parameter(description = "Data used to update category") @Valid @RequestBody CategoryDTO categoryDTO,
            @Parameter(description = "ID of category") @PathVariable UUID id
    ) {
        return categoryService.update(categoryDTO, id);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a category from the service", tags = {"categories"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    public boolean delete(
            @Parameter(description = "ID of category") @PathVariable UUID id
    ) {
        return categoryService.delete(id);
    }
}