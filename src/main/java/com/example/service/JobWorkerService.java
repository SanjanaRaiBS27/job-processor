package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.model.JobRequest;
import com.example.model.JobResult;

@Service
public class JobWorkerService {
    @Autowired
    private JobService jobService;

    @Scheduled(fixedDelay = 1000)
    public void processJobs() {
        JobRequest jobRequest = jobService.dequeueJob();
        if (jobRequest != null) {
            JobResult result = new JobResult(jobRequest.getUserId(), jobRequest.getContent());
            jobService.saveResult(result);
        }
    }
}