
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.util.FileSystemUtils;

import javax.jms.Message;
import java.io.File;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // Clean out any ActiveMQ data from a previous run
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        // create message
        MessageCreator messageCreator = session -> session.createTextMessage("ping!");

        final Runnable receiver = () -> {
            // synchronous reception
            System.out.println("====> Waiting for new message.");
            jmsTemplate.setReceiveTimeout(100);
            final Message message = jmsTemplate.receive("default");
            if (message == null){
                System.out.println("====> Synchronous reception: no message received");
            } else {
                System.out.println("====> Synchronous reception: " + message);
            }
        };
        new Thread(receiver).start();

        // Send a message
        System.out.println("====> Sending a new message.");
        jmsTemplate.send("default", messageCreator);
    }

}
