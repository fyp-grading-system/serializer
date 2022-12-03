package com.fypgradingsystem.serializer.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {
  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void send(Message message) {
    rabbitTemplate.convertAndSend(message.getQueue(), message.getMessage());
  }
}
