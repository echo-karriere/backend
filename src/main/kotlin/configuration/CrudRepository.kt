package no.echokarriere.configuration

interface CrudRepository<E, I> {
    suspend fun selectAll(): List<E>
    suspend fun select(id: I): E?
    suspend fun insert(value: E): E?
    suspend fun update(value: E): E?
    suspend fun delete(id: I): Boolean
}
