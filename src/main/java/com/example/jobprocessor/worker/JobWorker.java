package com.example.jobprocessor.worker;

import com.example.jobprocessor.service.JobQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobWorker implements CommandLineRunner {

    @Autowired
    private JobQueueService queueService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                try {
                    String data = queueService.take();
                    String processed = process(data);
                    redisTemplate.opsForValue().set("processed:" + data, processed);
                    System.out.println("Processed and cached: " + processed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String process(String input) {
        return "processed_" + input.toUpperCase();
    }
}
