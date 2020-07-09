package no.echokarriere.backend.namespace

import com.expediagroup.graphql.spring.operations.Mutation
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class NamespaceQueryResolver(
    private val namespaceRepository: NamespaceRepository
) : Query {
    fun namespaces(): List<NamespaceEntity> = namespaceRepository.selectAll()
}

@Component
class NamespaceMutationResolver(
    private val namespaceRepository: NamespaceRepository
) : Mutation {
    fun createNamespace(title: String) =
        namespaceRepository.insert(NamespaceEntity(title = title, description = "Test", namespace = title.toLowerCase()))
}
