package com.demo.consultation.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.spring.server")
@EnableJpaRepositories(basePackages = "org.spring.server.db.model")
@EntityScan("org.spring.server.db.model")
public class ServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
