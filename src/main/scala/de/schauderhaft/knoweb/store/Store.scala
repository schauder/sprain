package de.schauderhaft.knoweb.store

import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql._
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.session._
import java.util.UUID

class Store(sessionFunc : => Session) {

    implicit def session = sessionFunc

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
}

object Nodes extends Table[(String, String)]("node") {
    def id = column[String]("id", O.PrimaryKey)
    def name = column[String]("name", O.NotNull)

    def * = id ~ name
    private def uName = index("uk_name", name, unique = true)
}

object Links extends Table[(String, String, String, String)]("link") {
    def id = column[String]("id", O.PrimaryKey)
    def from = column[String]("from", O.NotNull)
    def link = column[String]("name", O.NotNull)
    def to = column[String]("to", O.NotNull)

    def * = id ~ from ~ link ~ to
    private def uFromLinkTo = index("uk_FromLinkTo", from ~ link ~ to, unique = true)

}
