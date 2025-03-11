package com.example.hseshellfinanceapp.ui;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.hseshellfinanceapp"})
public class ShellApplication {

    @Autowired
    private Terminal terminal;

    public static void main(String[] args) {
        SpringApplication.run(ShellApplication.class, args);
    }

    @Bean
    public PromptProvider promptProvider() {
        return () -> org.jline.utils.AttributedString.fromAnsi("\nfinance-tracker:> ");
    }
}
