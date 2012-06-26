package de.schauderhaft.sprain.db

import de.schauderhaft.sprain.store.Store
import java.util.UUID
import org.scalaquery.ql.TypeMapper.StringTypeMapper
import org.scalaquery.ql.basic.BasicDriver.Implicit.baseColumnToColumnOps
import org.scalaquery.ql.basic.BasicDriver.Implicit.columnBaseToInsertInvoker
import org.scalaquery.ql.basic.BasicDriver.Implicit.queryToDeleteInvoker
import org.scalaquery.ql.basic.BasicDriver.Implicit.queryToQueryInvoker
import org.scalaquery.ql.basic.BasicDriver.Implicit.tableToQuery
import org.scalaquery.ql.basic.BasicDriver.Implicit.valueToConstColumn
import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.session.Database.threadLocalSession

class PersistentStore extends Store {

    def allNodes() = {
        val nodes = for (n <- Nodes) yield n.name
        nodes.list.toSet
    }

    def allLinks() = (
        for (l <- Links)
            yield (l.from, l.link, l.to))
        .list.toSet

    /** adds a node to the store */
    def add(node : String) = {
        (Nodes.where(_.name === node) map (_.id)).
            firstOption match {
                case Some(id) => id
                case None =>
                    val id = UUID.randomUUID.toString
                    Nodes.insert(id, node)
                    id
            }
    }

    /** adds a link between to nodes to the store */
    def add(from : String, link : String, to : String) = {
        val id = UUID.randomUUID.toString
        Links.insert(id, from, link, to)
        id
    }

    /** removes a node from the store, does nothing when the node is not present in the store*/
    def deleteNode(id : String) {
        Nodes.where(_.id === id).delete
    }

    /** removes a link from the store, does nothing when the node is not present in the store*/
    def deleteLink(id : String) {

        val linksToDelete = for {
            l <- Links
            if l.id === id
        } yield l

        linksToDelete.delete
    }
}

object Nodes extends Table[(String, String)]("node") {
    def id = column[String]("id", O.PrimaryKey)
    def name = column[String]("name", O.NotNull)

    def * = id ~ name
    private def uName = index("uk_name", name, unique = true)
}

object Links extends Table[(String, String, String, String)]("link") {
    def id = column[String]("id", O.PrimaryKey)
    def from = column[String]("from_id", O.NotNull)
    def link = column[String]("name", O.NotNull)
    def to = column[String]("to_id", O.NotNull)

    def * = id ~ from ~ link ~ to
    private def ukFromLinkTo = index("uk_FromLinkTo", from ~ link ~ to, unique = true)
    private def fkFrom = foreignKey("fk_Link_Node_From", from, Nodes)(_.id)
    private def fkTo = foreignKey("fk_Link_Node_To", to, Nodes)(_.id)
}
