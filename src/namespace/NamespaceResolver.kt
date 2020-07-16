package no.echokarriere.namespace

import java.util.UUID

@Suppress("unused") // Used by GraphQL via reflection
class NamespaceQueryResolver(
    private val namespaceRepository: NamespaceRepository
) {
    suspend fun namespaces(): List<NamespaceEntity> = namespaceRepository.selectAll()
    suspend fun namespaceById(id: UUID): NamespaceEntity? = namespaceRepository.selectOne(id)
}

@Suppress("unused") // Used by GraphQL via reflection
class NamespaceMutationResolver(
    private val namespaceRepository: NamespaceRepository
) {
    fun createNamespace(title: String) =
        namespaceRepository.insert(NamespaceEntity(title = title, description = "Test", namespace = title.toLowerCase()))
}
