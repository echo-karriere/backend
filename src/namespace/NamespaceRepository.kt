package no.echokarriere.namespace

class NamespaceRepository {
    fun selectAll(): List<NamespaceEntity> = TODO()

    fun selectOne(id: Int): NamespaceEntity? = TODO()

    fun insert(namespace: NamespaceEntity): NamespaceEntity = TODO()

    fun update(id: Int, namespace: NamespaceEntity): NamespaceEntity? = TODO()

    fun delete(id: Int): Int = TODO()
}
