package no.echokarriere.backend.configuration;

import no.echokarriere.backend.category.CategoryEntity;
import no.echokarriere.backend.category.CategoryRepository;
import no.echokarriere.backend.company.CompanyEntity;
import no.echokarriere.backend.company.CompanyRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DevDataLoader implements ApplicationRunner {
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    public DevDataLoader(CategoryRepository categoryRepository, CompanyRepository companyRepository) {
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (System.getenv("DEV_MODE") != null) {
            initializeDevCategories();
            initializeDevCompanies();
        }
    }

    private void initializeDevCategories() {
        if (!categoryRepository.selectAll().isEmpty()) return;
        var categories = List.of(
                new CategoryEntity(UUID.fromString("af53312e-f87b-47e8-a14d-a63537ec7ddd"), "Nyheter", "Sider med nyheter", "news", OffsetDateTime.now(), null),
                new CategoryEntity(UUID.fromString("daf2fe8a-54c8-4d02-b201-ac1bf121b0ac"), "For studenter", "Sider for studenter", "for-studenter", OffsetDateTime.now(), null),
                new CategoryEntity(UUID.fromString("bdddaaea-e8e0-42cc-aaf4-87014163a43f"), "For bedrifter", "Sider for bedrifter", "for-bedrifter", OffsetDateTime.now(), null)
        );
        categories.forEach(categoryRepository::create);
    }

    private void initializeDevCompanies() {
        if (!companyRepository.selectAll().isEmpty()) return;
        var companies = List.of(
                new CompanyEntity(UUID.fromString("a04e620b-b9b9-4012-a202-83c15c96fc77"), "Acme Corporation", "acme.com", OffsetDateTime.now(), null),
                new CompanyEntity(UUID.fromString("40468aaa-e5ae-4d51-b0d2-931842d2702f"), "Globex Corporation", "globex.org", OffsetDateTime.now(), null),
                new CompanyEntity(UUID.fromString("337e1370-cb5c-45b6-ad6c-fdd925a27313"), "Umbrella Corporation", "umbrella.com", OffsetDateTime.now(), null)
        );
        companies.forEach(companyRepository::create);
    }
}
