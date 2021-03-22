package no.echokarriere.backend.company;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class CompanyMapper implements RowMapper<CompanyEntity> {
    @Override
    public CompanyEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return CompanyEntity.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .homepage(rs.getString("homepage"))
                .createdAt(rs.getObject("created_at", OffsetDateTime.class))
                .modifiedAt(rs.getObject("modified_at", OffsetDateTime.class))
                .build();
    }
}
