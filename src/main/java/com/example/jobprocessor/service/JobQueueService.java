package com.example.jobprocessor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobQueueService {
    private static final Logger logger = LoggerFactory.getLogger(JobQueueService.class);
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void push(String data) {
        try {
            // Add to processing queue
            queue.offer(data);
            logger.info("Added job to queue: {}", data);

            // Store in Redis for history
            String userId = data.split(":")[0];  // Assuming data format is "userId:jobData"
            String historyKey = "job:history:" + userId;
            
            // Check if this exact job already exists
            List<String> existingJobs = redisTemplate.opsForList().range(historyKey, 0, -1);
            if (existingJobs == null || !existingJobs.contains(data)) {
                redisTemplate.opsForList().rightPush(historyKey, data);
                logger.info("Added job to history for user: {}", userId);
            }
        } catch (Exception e) {
            logger.error("Error pushing job to queue: {}", e.getMessage());
            throw new RuntimeException("Failed to process job", e);
        }
    }

    public String take() throws InterruptedException {
        try {
            String data = queue.take();
            logger.info("Taken job from queue: {}", data);
            return data;
        } catch (InterruptedException e) {
            logger.error("Error taking job from queue: {}", e.getMessage());
            throw e;
        }
    }

    public List<String> getJobHistory(String userId) {
        try {
            String key = "job:history:" + userId;
            List<String> history = redisTemplate.opsForList().range(key, 0, -1);
            logger.info("Retrieved job history for user: {}", userId);
            return history != null ? history : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error retrieving job history: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve job history", e);
        }
    }

    public String getProcessedResult(String jobId) {
        try {
            String key = "processed:" + jobId;
            String result = redisTemplate.opsForValue().get(key);
            logger.info("Retrieved processed result for job: {}", jobId);
            return result;
        } catch (Exception e) {
            logger.error("Error retrieving processed result: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve processed result", e);
        }
    }
}
