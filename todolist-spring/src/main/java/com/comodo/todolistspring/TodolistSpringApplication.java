package com.comodo.todolistspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.comodo.todolistspring.repository")
public class TodolistSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistSpringApplication.class, args);
    }

}
