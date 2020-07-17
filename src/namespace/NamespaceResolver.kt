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
    suspend fun createNamespace(data: NamespaceDTO): NamespaceEntity? = namespaceRepository.insert(data)
    suspend fun deleteNamespace(id: UUID): Boolean = namespaceRepository.delete(id)
    suspend fun updateNamespace(id: UUID, data: NamespaceDTO): NamespaceEntity? = namespaceRepository.update(id, data)
}
