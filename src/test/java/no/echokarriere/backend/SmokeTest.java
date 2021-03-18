package no.echokarriere.backend;

import no.echokarriere.backend.category.CategoryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmokeTest {
    @Autowired
    private CategoryController categoryController;

    @Test
    void contextLoads() {
        assertThat(categoryController).isNotNull();
    }
}
