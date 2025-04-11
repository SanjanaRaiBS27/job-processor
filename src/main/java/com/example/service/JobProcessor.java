package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.model.JobRequest;
import com.example.model.JobResult;
import com.example.repository.JobResultRepository;

@Service
public class JobProcessor {

    private static final String JOB_QUEUE = "jobQueue";

    @Autowired
    private RedisTemplate<String, JobRequest> jobQueueTemplate;

    @Autowired
    private JobCacheService cacheService;

    @Autowired
    private JobResultRepository jobResultRepository;

    @Scheduled(fixedDelay = 1000)
    public void processJobs() {
        JobRequest jobRequest = jobQueueTemplate.opsForList().rightPop(JOB_QUEUE);
        if (jobRequest != null) {
            String processedContent = "Processed: " + jobRequest.getContent().toUpperCase();
            JobResult jobResult = new JobResult(jobRequest.getUserId(), jobRequest.getContent(), processedContent);
            
            // Save to repository
            jobResultRepository.save(jobResult);
            
            // Cache the result
            cacheService.cacheResult(jobRequest.getUserId(), processedContent);
        }
    }
} 