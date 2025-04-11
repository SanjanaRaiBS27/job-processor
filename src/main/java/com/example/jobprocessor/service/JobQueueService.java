package com.example.jobprocessor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobQueueService {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void push(String data) {
        // Only store in Redis for history, not in the queue
        String key = "job:history:" + data.split(":")[0];  // Assuming data format is "userId:jobData"
        // Check if this exact job already exists
        List<String> existingJobs = redisTemplate.opsForList().range(key, 0, -1);
        if (existingJobs == null || !existingJobs.contains(data)) {
            redisTemplate.opsForList().rightPush(key, data);
        }
    }

    public String take() throws InterruptedException {
        return queue.take();
    }

    public List<String> getJobHistory(String userId) {
        String key = "job:history:" + userId;
        List<String> history = redisTemplate.opsForList().range(key, 0, -1);
        return history != null ? history : new ArrayList<>();
    }
}
