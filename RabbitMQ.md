# RabbitMQ with Spring Boot

## Introduction to RabbitMQ
RabbitMQ is a powerful message broker that enables distributed applications to communicate asynchronously. It implements the Advanced Message Queuing Protocol (AMQP) and supports various messaging patterns such as publish/subscribe, request/reply, and work queues. RabbitMQ is widely used in microservices architectures to decouple services, improve scalability, and enhance reliability.

## Why Use RabbitMQ with Spring Boot?
Spring Boot simplifies the integration of RabbitMQ by providing auto-configuration, easy-to-use templates, and annotation-driven consumers. Some key advantages include:
- **Asynchronous Processing:** Reduces the dependency on synchronous interactions.
- **Reliability:** Messages are persisted and can be replayed if needed.
- **Scalability:** Allows distributed processing across multiple services.
- **Decoupling:** Services can operate independently without being tightly coupled.
- **Flexible Routing:** Supports direct, fanout, topic, and headers exchanges for message routing.

## RabbitMQ Architecture
RabbitMQ operates based on the **Producer-Exchange-Queue-Consumer** model:

![This is a image](https://raw.githubusercontent.com/RajeevBandi/SpringBoot_Beginner/main/Jdbc_manual_configuration.png)


1. **Producer:** The component that sends messages.
2. **Exchange:** Routes messages based on binding rules and routing keys.
3. **Queue:** Stores messages until they are consumed.
4. **Consumer:** The component that receives and processes messages.
5. **Binding:** The connection between an exchange and a queue.
6. **Broker:** The RabbitMQ server that manages queues and exchanges.

### Message Flow in RabbitMQ
1. A producer sends a message to an exchange.
2. The exchange routes the message to the appropriate queue(s) based on its type.
3. A consumer retrieves the message from the queue and processes it.
4. The consumer acknowledges the message, confirming successful processing.

## Exchange Types in RabbitMQ
- **Direct Exchange:** Routes messages to a queue with a matching routing key.
- **Fanout Exchange:** Routes messages to all bound queues.
- **Topic Exchange:** Routes messages based on wildcard matching of routing keys.
- **Headers Exchange:** Routes messages based on header attributes.

---

## Setting Up RabbitMQ
### Prerequisites
- Java 17 or later
- Spring Boot 3+
- RabbitMQ installed (Docker or local installation)
- Maven or Gradle

### Docker Installation
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```
- Management UI: `http://localhost:15672/` (Default user: guest, Password: guest)

## Adding Dependencies
### Maven
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### Gradle
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
}
```

## Spring Boot Configuration
### application.properties
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### Defining a Message Queue
```java
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    // Define a durable queue named "example.queue"
    @Bean
    public Queue exampleQueue() {
        return new Queue("example.queue", true);
    }
}
```

### Configuring Exchanges and Bindings
```java
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQExchangeConfig {
    
    // Define a direct exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("example.direct.exchange");
    }

    // Bind the queue to the exchange using a routing key
    @Bean
    public Binding binding(Queue exampleQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(exampleQueue).to(directExchange).with("routing.key");
    }
}
```

## Producer (Message Publisher)
```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    
    private static final String QUEUE_NAME = "example.queue";
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    // Method to send a message to the queue
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        System.out.println("Sent: " + message);
    }
}
```

## Consumer (Message Listener)
```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
    
    // Listener method that processes messages from the queue
    @RabbitListener(queues = "example.queue")
    public void receiveMessage(String message) {
        System.out.println("Received: " + message);
    }
}
```

## Message Acknowledgment
RabbitMQ allows for manual and automatic acknowledgment:
```java
@RabbitListener(queues = "example.queue", ackMode = "MANUAL")
public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
    try {
        System.out.println("Received: " + message);
        channel.basicAck(tag, false); // Acknowledge successful processing
    } catch (Exception e) {
        channel.basicNack(tag, false, true); // Requeue message on failure
    }
}
```

## Dead Letter Queues (DLQ)
Dead Letter Queues handle failed messages.
```java
@Bean
public Queue dlq() {
    return QueueBuilder.durable("example.dlq").build();
}
```

## Testing the RabbitMQ Implementation
Create a REST Controller to trigger the producer:
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    
    @Autowired
    private MessageProducer messageProducer;
    
    // Endpoint to send a message via HTTP request
    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        messageProducer.sendMessage(message);
        return "Message sent: " + message;
    }
}
```

### API Call Example
```bash
curl "http://localhost:8080/send?message=HelloRabbitMQ"
```

## Advanced Features
- **Message Persistence**: Ensures messages are not lost after RabbitMQ restarts.
- **Retry Mechanism**: Implement retries for failed messages.
- **Priority Queues**: Assign priority levels to messages.
- **Delayed Message Exchange**: Delay message processing for a specific time.

## Conclusion
This guide demonstrates how to integrate RabbitMQ with Spring Boot for asynchronous messaging. It includes configuring RabbitMQ, sending messages, consuming them, and handling advanced features like acknowledgments, DLQs, and exchanges.

## References
- [RabbitMQ Official Documentation](https://www.rabbitmq.com/documentation.html)
- [Spring AMQP Documentation](https://docs.spring.io/spring-amqp/reference/html/)
