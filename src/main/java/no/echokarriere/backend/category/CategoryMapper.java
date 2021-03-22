package no.echokarriere.backend.category;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class CategoryMapper implements RowMapper<CategoryEntity> {
    @Override
    public CategoryEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return CategoryEntity.builder()
                .id(UUID.fromString(rs.getString("id")))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .slug(rs.getString("slug"))
                .createdAt(rs.getObject("created_at", OffsetDateTime.class))
                .modifiedAt(rs.getObject("modified_at", OffsetDateTime.class))
                .build();
    }
}
