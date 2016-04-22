package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        log.info(quote.toString());

        // Async version
        AsyncRestTemplate asyncTemplate = new AsyncRestTemplate();
        final ListenableFuture<ResponseEntity<Quote>> quoteFuture = asyncTemplate.getForEntity("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        quoteFuture.addCallback(
                success -> log.info("async: " + quote),
                failure -> log.error("Async error", failure));
    }
}