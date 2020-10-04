package no.echokarriere.configuration

interface CrudRepository<E, I> {
    suspend fun selectAll(): List<E>
    suspend fun select(id: I): E?
    suspend fun insert(entity: E): E?
    suspend fun update(entity: E): E?
    suspend fun delete(id: I): Boolean
}
