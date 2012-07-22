package de.schauderhaft.sprain.db.schema
import org.scalaquery.ql.basic.{ BasicTable => Table }

/*
create table node(
 id varchar(255) PRIMARY KEY ,
 name varchar(255) not null
);


create table link(
 id varchar(255) PRIMARY KEY ,
 from_id varchar(255) not null,
 link varchar(255) not null,
 to_id varchar(255) not null,
 -- CONSTRAINT ukFromLinkTo UNIQUE (from_id, link, to_id),
 CONSTRAINT fkFrom FOREIGN KEY (from_id) REFERENCES node (id),
 CONSTRAINT fkTo FOREIGN KEY (from_id) REFERENCES node (id)
);
*/

object Nodes extends Table[(String, String)]("node") {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name", O.NotNull)

  def * = id ~ name
  private def uName = index("uk_name", name, unique = true)
}

