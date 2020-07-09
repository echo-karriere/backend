package no.echokarriere.backend.namespace

import org.jdbi.v3.sqlobject.SingleValue
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.stereotype.Repository

@Repository
interface NamespaceRepository {
    @SqlQuery("SELECT * FROM namespace")
    fun selectAll(): List<Namespace>

    @SqlQuery("SELECT * FROM namespace WHERE id = :id")
    @SingleValue
    fun selectOne(id: Int): Namespace?

    @SqlUpdate(
        """
        INSERT INTO namespace(title, description, namespace)
        VALUES (:title, :description, :namespace)
        """
    )
    @GetGeneratedKeys
    @RegisterKotlinMapper(Namespace::class)
    fun insert(@BindBean namespace: Namespace): Namespace

    @SqlUpdate(
        """
        UPDATE namespace
        SET title = :title, description = :description, namespace = :namespace
        WHERE id = :id
        RETURNING *
    """
    )
    @GetGeneratedKeys
    @RegisterKotlinMapper(Namespace::class)
    fun update(id: Int, @BindBean namespace: Namespace): Namespace?

    @SqlUpdate("DELETE FROM namespace WHERE ID = :id")
    fun delete(id: Int): Int
}