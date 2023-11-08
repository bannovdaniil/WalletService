package ru.ylab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.ylab.customenableannotation.aop.annotation.EnableCustomLogger;

/**
 * Spring Boot main class starter
 */
@SpringBootApplication
@EnableCustomLogger
public class WalletServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }

}
