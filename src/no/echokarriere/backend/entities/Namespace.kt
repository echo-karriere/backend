package no.echokarriere.backend.entities

import kotlinx.serialization.Serializable
import org.jdbi.v3.core.mapper.reflect.ColumnName

@Serializable
data class Namespace(
    @ColumnName("id")
    val id: Int = 0,
    @ColumnName("title")
    val title: String,
    @ColumnName("description")
    val description: String,
    @ColumnName("namespace")
    val namespace: String
)
