package no.echokarriere.backend.namespace

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.UUID

data class Namespace(
    @ColumnName("id")
    val id: UUID = UUID.randomUUID(),
    @ColumnName("title")
    val title: String,
    @ColumnName("description")
    val description: String,
    @ColumnName("namespace")
    val namespace: String
)
