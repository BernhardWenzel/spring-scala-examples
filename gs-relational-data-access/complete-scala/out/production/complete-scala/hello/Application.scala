package hello

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jdbc.core.JdbcTemplate

import collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

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

        // Split up the array of whole names into an array of first/last names
        val splitUpNames: mutable.Buffer[Array[AnyRef]] = ListBuffer("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").map(_.split(" ")).asInstanceOf[mutable.Buffer[Array[AnyRef]]]

        // Use a Java 8 stream to print out each tuple of the list
      for(name <- splitUpNames)
        splitUpNames.foreach{ case(Array(name)) => log.info("Inserting customer record for %s %s".format(name(0), name(1)))}

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames.asJava)

        log.info("Querying for customer records where first_name = 'Josh':")
//        jdbcTemplate.query(
//                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
//                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
//        ).forEach(customer -> log.info(customer.toString()))

    }
}

object Application extends App {
  SpringApplication.run(classOf[Application], args:_*)
}
