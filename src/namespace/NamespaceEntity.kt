package no.echokarriere.namespace

import java.util.UUID

data class NamespaceEntity(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val namespace: String
)
