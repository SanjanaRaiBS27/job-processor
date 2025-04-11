package com.example.jobprocessor.worker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.jobprocessor.service.JobQueueService;

@Component
public class JobWorker implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(JobWorker.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JobQueueService queueService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) {
        logger.info("Starting job worker...");
        new Thread(() -> {
            while (true) {
                try {
                    String data = queueService.take();
                    logger.info("Processing job: {}", data);
                    
                    String processed = process(data);
                    String jobId = UUID.randomUUID().toString();
                    
                    // Store processed result with metadata
                    String result = String.format("%s|%s|%s", 
                        processed,
                        LocalDateTime.now().format(formatter),
                        jobId
                    );
                    
                    redisTemplate.opsForValue().set("processed:" + data, result);
                    logger.info("Processed and cached job: {} with result: {}", data, result);
                    
                } catch (InterruptedException e) {
                    logger.error("Worker interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("Error processing job: {}", e.getMessage());
                }
            }
        }, "job-worker-thread").start();
    }

    private String process(String input) {
        try {
            // Simulate some processing time
            Thread.sleep(1000);
            
            // Example processing logic:
            // 1. Split the input (assuming format: "userId:jobData")
            String[] parts = input.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid input format. Expected 'userId:jobData'");
            }
            
            String userId = parts[0];
            String jobData = parts[1];
            
            // 2. Process the data (example: transform to uppercase and add timestamp)
            String processed = String.format("%s_%s_%s",
                jobData.toUpperCase(),
                userId,
                LocalDateTime.now().format(formatter)
            );
            
            return processed;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("Error processing job: " + e.getMessage(), e);
        }
    }
}
