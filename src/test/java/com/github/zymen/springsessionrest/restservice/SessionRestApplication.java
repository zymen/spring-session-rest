package com.github.zymen.springsessionrest.restservice;

import com.github.zymen.springsessionrest.EnableRestHttpSession;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@EnableRestHttpSession
@SpringBootApplication
public class SessionRestApplication {

    public static void main(String[] args) {
        run(SessionRestApplication.class, args);
    }
}
