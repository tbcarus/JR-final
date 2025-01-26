package ru.tbcarus.jrfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JrFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JrFinalApplication.class, args);
    }

}
