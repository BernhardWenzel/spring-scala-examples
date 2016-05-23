package hello

import java.sql.ResultSet

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jdbc.core.{JdbcTemplate, RowMapper}

import collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

@SpringBootApplication
class Application extends CommandLineRunner {

  @Autowired
  var jdbcTemplate: JdbcTemplate = _


  val log: Logger = LoggerFactory.getLogger(classOf[Application])

  override def run(args: String*): Unit = {
    log.info("Creating tables")
    jdbcTemplate.execute("DROP TABLE customers IF EXISTS")
    jdbcTemplate.execute("CREATE TABLE customers(" +
      "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))")

    val splitUpNames = ListBuffer("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").map(_.split(" "))
    splitUpNames.foreach(name => log.info("Inserting customer record for %s %s".format(name(0), name(1))))

    jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames.asInstanceOf[mutable.Buffer[Array[AnyRef]]].asJava)

    log.info("Querying for customer records where first_name = 'Josh':")
    jdbcTemplate.query(
      "SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
      Array("Josh").asInstanceOf[Array[AnyRef]],
      // no Java 8 Lambda support in Scala pre 2.12
      new RowMapper[Customer]{
        override def mapRow(rs: ResultSet, rowNum: Int): Customer = new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
      })
      // Works in Scala 2.12
      // (rs: ResultSet, rowNum: Int) => new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))    )
      .asScala.foreach((customer:Customer) => log.info(customer.toString))
  }
}

object Application extends App {
  SpringApplication.run(classOf[Application], args:_*)
}
