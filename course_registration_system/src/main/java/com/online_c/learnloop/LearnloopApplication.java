package com.online_c.learnloop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class LearnloopApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnloopApplication.class, args);
    }
}

