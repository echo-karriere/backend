package no.echokarriere.namespace

class NamespaceQueryResolver(
    private val namespaceRepository: NamespaceRepository
) {
    fun namespaces(): List<NamespaceEntity> = namespaceRepository.selectAll()
}

class NamespaceMutationResolver(
    private val namespaceRepository: NamespaceRepository
) {
    fun createNamespace(title: String) =
        namespaceRepository.insert(NamespaceEntity(title = title, description = "Test", namespace = title.toLowerCase()))
}
