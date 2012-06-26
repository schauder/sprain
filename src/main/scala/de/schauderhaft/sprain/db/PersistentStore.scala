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
import org.scalaquery.session.Database.threadLocalSession
import de.schauderhaft.sprain.db.schema.Nodes
import de.schauderhaft.sprain.db.schema.Links

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

