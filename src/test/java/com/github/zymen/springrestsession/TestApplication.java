package com.github.zymen.springrestsession;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@EnableRestHttpSession
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }
}
