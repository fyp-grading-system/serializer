package com.fypgradingsystem.serializer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class SerializerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SerializerApplication.class, args);
  }
}
