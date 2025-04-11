package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.JobRequest;
import com.example.model.JobResult;
import com.example.repository.JobResultRepository;
import com.example.service.JobCacheService;
import com.example.service.JobQueueService;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobQueueService queueService;

    @Autowired
    private JobCacheService cacheService;

    @Autowired
    private JobResultRepository jobResultRepository;

    @PostMapping
    public String submitJob(@RequestBody JobRequest jobRequest) {
        queueService.enqueueJob(jobRequest);
        return "Job submitted successfully!";
    }

    @GetMapping("/{userId}")
    public String getLatestResult(@PathVariable String userId) {
        // Get from Redis cache
        String cachedResult = cacheService.getCachedResult(userId);
        return cachedResult;
    }

    @GetMapping("/{userId}/history")
    public List<JobResult> getJobHistory(@PathVariable String userId) {
        return jobResultRepository.findByUserId(userId);
    }
}