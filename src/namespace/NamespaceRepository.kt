package no.echokarriere.namespace

interface NamespaceRepository {
    fun selectAll(): List<NamespaceEntity>

    fun selectOne(id: Int): NamespaceEntity?

    fun insert(namespace: NamespaceEntity): NamespaceEntity

    fun update(id: Int, namespace: NamespaceEntity): NamespaceEntity?

    fun delete(id: Int): Int
}
