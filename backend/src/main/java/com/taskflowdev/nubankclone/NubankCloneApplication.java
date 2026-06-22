package com.taskflowdev.nubankclone;

import com.taskflowdev.nubankclone.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class NubankCloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(NubankCloneApplication.class, args);
    }
}
