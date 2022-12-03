package com.fypgradingsystem.serializer.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Publisher {
  @Value("${queue.name}")
  private String message;

  @Bean
  public Queue queue() {
    return new Queue(message, true);
  }
}
