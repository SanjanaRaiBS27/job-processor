package com.example.jobprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.jobprocessor"})
public class JobProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobProcessorApplication.class, args);
    }
}
