package de.schauderhaft.sprain.db.schema
import org.scalaquery.ql.basic.{ BasicTable => Table }

object Nodes extends Table[(String, String)]("node") {
    def id = column[String]("id", O.PrimaryKey)
    def name = column[String]("name", O.NotNull)

    def * = id ~ name
    private def uName = index("uk_name", name, unique = true)
}

