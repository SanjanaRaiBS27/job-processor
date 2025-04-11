package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.model.JobRequest;

@Service
public class JobQueueService {

    private static final String JOB_QUEUE = "jobQueue";

    @Autowired
    private RedisTemplate<String, JobRequest> jobQueueTemplate;

    public void enqueueJob(JobRequest jobRequest) {
        jobQueueTemplate.opsForList().leftPush(JOB_QUEUE, jobRequest);
    }
}