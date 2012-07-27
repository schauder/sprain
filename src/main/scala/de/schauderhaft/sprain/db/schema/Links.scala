package de.schauderhaft.sprain.db.schema
import org.scalaquery.ql.extended.{ ExtendedTable => Table }

object Links extends Table[(String, String, String, String)]("link") {
    def id = column[String]("id", O.PrimaryKey)
    def from = column[String]("from_id", O.NotNull)
    def link = column[String]("link", O.NotNull)
    def to = column[String]("to_id", O.NotNull)

    def * = id ~ from ~ link ~ to
    private def ukFromLinkTo = index("uk_FromLinkTo", from ~ link ~ to, unique = true)
    private def fkFrom = foreignKey("fk_Link_Node_From", from, Nodes)(_.id)
    private def fkTo = foreignKey("fk_Link_Node_To", to, Nodes)(_.id)
}