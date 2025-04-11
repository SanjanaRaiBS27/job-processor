package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.model.JobRequest;
import com.example.model.JobResult;
import com.example.repository.JobResultRepository;

@Service
public class JobWorkerService {

    private static final String JOB_QUEUE = "jobQueue";

    @Autowired
    private RedisTemplate<String, JobRequest> jobQueueTemplate;

    @Autowired
    private JobCacheService cacheService;

    @Autowired
    private JobResultRepository jobResultRepository;

    @Scheduled(fixedRate = 5000)
    public void processJobs() {
        JobRequest jobRequest = jobQueueTemplate.opsForList().rightPop(JOB_QUEUE);
        if (jobRequest != null) {
            System.out.println("Processing job for user: " + jobRequest.getUserId());
            String processedContent = "Processed: " + jobRequest.getContent().toUpperCase();
            
            // Store in Redis for quick access
            cacheService.cacheResult(jobRequest.getUserId(), processedContent);
            
            // Store in Redis for permanent storage
            JobResult jobResult = new JobResult(
                jobRequest.getUserId(),
                jobRequest.getContent(),
                processedContent
            );
            jobResultRepository.save(jobResult);
        }
    }
}