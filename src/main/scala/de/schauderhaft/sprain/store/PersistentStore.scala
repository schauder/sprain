package de.schauderhaft.sprain.store

import java.util.UUID

import org.scalaquery.ql.TypeMapper.StringTypeMapper
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.session.Session
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
    def add(node : String) {
        Nodes.insert(UUID.randomUUID.toString, node)
    }

    /** adds a link between to nodes to the store */
    def add(from : String, link : String, to : String) {
        Links.insert(UUID.randomUUID.toString, from, link, to)
    }

    /** removes a node from the store, does nothing when the node is not present in the store*/
    def deleteNode(node : String) {
        Nodes.where(_.name === node).delete
    }

    /** removes a link from the store, does nothing when the node is not present in the store*/
    def deleteLink(from : String,
                   link : String,
                   to : String) {

        val linksToDelete = for {
            l <- Links
            if l.from === from
            if l.link === link
            if l.to === to
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
