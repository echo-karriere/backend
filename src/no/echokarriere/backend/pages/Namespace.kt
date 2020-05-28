package no.echokarriere.backend.pages

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class Namespace(
    @ColumnName("id")
    val id: Int,
    @ColumnName("title")
    val title: String,
    @ColumnName("description")
    val description: String,
    @ColumnName("namespace")
    val namespace: String
)
