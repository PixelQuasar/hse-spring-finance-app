package com.example.hseshellfinanceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {"com.example.hseshellfinanceapp"},
        proxyBeanMethods = false,
        exclude = {
                StandardCommandsAutoConfiguration.class
        }
)
public class HseShellFinanceAppApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HseShellFinanceAppApplication.class);
        application.run(args);
    }
}
