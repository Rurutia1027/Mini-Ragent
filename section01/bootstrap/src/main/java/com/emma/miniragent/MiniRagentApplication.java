package com.emma.miniragent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.emma.miniragent")
public class MiniRagentApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniRagentApplication.class, args);
    }
}
