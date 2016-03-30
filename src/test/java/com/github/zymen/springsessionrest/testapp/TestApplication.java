package com.github.zymen.springsessionrest.testapp;

import com.github.zymen.springsessionrest.EnableRestHttpSession;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@EnableRestHttpSession
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }
}
