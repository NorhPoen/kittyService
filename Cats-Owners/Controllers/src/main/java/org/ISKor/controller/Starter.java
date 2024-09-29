package org.ISKor.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.ISKor.repositories")
@EntityScan(basePackages = "org.ISKor.entity")
@SpringBootApplication(scanBasePackages = {"org.ISKor"})
public class Starter {
    public static void main(String[] args)  {
        SpringApplication.run(Starter.class, args);
    }
}
