package no.echokarriere.backend.namespace

import java.util.UUID
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class NamespaceEntity(
    @ColumnName("id")
    val id: UUID = UUID.randomUUID(),
    @ColumnName("title")
    val title: String,
    @ColumnName("description")
    val description: String,
    @ColumnName("namespace")
    val namespace: String
)
