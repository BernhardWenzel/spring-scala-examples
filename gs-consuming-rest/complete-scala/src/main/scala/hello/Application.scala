package hello

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.{CommandLineRunner, SpringApplication}
import org.springframework.http.ResponseEntity
import org.springframework.util.concurrent.{ListenableFuture, ListenableFutureCallback, SuccessCallback}
import org.springframework.web.client.{AsyncRestTemplate, RestTemplate}

@SpringBootApplication
class Application extends CommandLineRunner{
  val log: Logger = LoggerFactory.getLogger(classOf[Application])

  override def run(args: String*): Unit = {
    val restTemplate = new RestTemplate()

    // synchronous version
    val quote : Quote =  restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", classOf[Quote])
    log.info(quote.toString)

    // async version
    val asyncRestTemplate = new AsyncRestTemplate()
    val quoteFuture : ListenableFuture[ResponseEntity[Quote]] =  asyncRestTemplate.getForEntity("http://gturnquist-quoters.cfapps.io/api/random", classOf[Quote])

    quoteFuture.addCallback(new ListenableFutureCallback[ResponseEntity[Quote]]() {
      override def onSuccess(entity : ResponseEntity[Quote]) : Unit = log.info("async: " + entity.getBody.toString)
      override def onFailure(t : Throwable) : Unit = log.error("Async error", t)
    })
  }
}

object Application extends App {
  SpringApplication.run(classOf[Application], args:_*)
}
