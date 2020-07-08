package no.echokarriere.backend.entities

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDateTime

enum class Status {
    PUBLISHED, DRAFT, DELETED
}

data class Page(
    @ColumnName("id")
    val id: Int,
    @ColumnName("title")
    val title: String,
    @ColumnName("content")
    val content: String,
    @ColumnName("slug")
    val slug: String,
    @ColumnName("namespace")
    val namespace: Namespace,
    @ColumnName("status")
    val status: Status,
    @ColumnName("created_on")
    val createdOn: LocalDateTime = LocalDateTime.now(),
    @ColumnName("modified_on")
    val modifiedOn: LocalDateTime,
    @ColumnName("published_on")
    val publishedOn: LocalDateTime
)
