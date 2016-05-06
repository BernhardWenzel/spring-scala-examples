
package hello

import org.springframework.boot.{CommandLineRunner, SpringApplication}
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import org.springframework.util.FileSystemUtils
import javax.jms.{Message, Session, TextMessage}
import java.io.File

import org.springframework.beans.factory.annotation.Autowired

import scala.beans.BeanProperty

@SpringBootApplication
class Application extends CommandLineRunner {

  @BeanProperty
  @Autowired
  val jmsTemplate : JmsTemplate = null

  def run(args: String*) : Unit = {
    // Clean out any ActiveMQ data from a previous run
    FileSystemUtils.deleteRecursively(new File("activemq-data"))

    // create message
    val messageCreator : MessageCreator = new MessageCreator {
      override def createMessage(session: Session): Message = session.createTextMessage("ping!")
    }

    val receiver: Runnable = new Runnable {
      def run(): Unit ={
        // synchronous reception
        System.out.println("====> Waiting for new message.")
        jmsTemplate.setReceiveTimeout(100)
        jmsTemplate.receive("default") match {
          case m:TextMessage => println("====> Synchronous reception: " + m.getText)
          case _ => println("====> Synchronous reception: No message received")
        }
      }
    }

    new Thread(receiver).start()

    // Send a message
    System.out.println("====> Sending a new message.")
    jmsTemplate.send("default", messageCreator)
  }
}

object Application extends App {
  SpringApplication.run(classOf[Application], args:_*)
}
