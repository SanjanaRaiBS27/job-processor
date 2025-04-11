package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.model.JobRequest;
import com.example.model.JobResult;

@Service
public class JobService {
    private static final String QUEUE_KEY = "jobQueue";
    private static final String RESULT_KEY = "job_result:";
    private static final String SET_KEY = "job_result_set:";
    private static final String CACHE_KEY = "processed:";

    @Autowired
    private RedisTemplate<String, JobRequest> queueTemplate;

    @Autowired
    private RedisTemplate<String, JobResult> resultTemplate;

    @Autowired
    private RedisTemplate<String, String> cacheTemplate;

    public void enqueueJob(JobRequest jobRequest) {
        queueTemplate.opsForList().rightPush(QUEUE_KEY, jobRequest);
    }

    public JobRequest dequeueJob() {
        return queueTemplate.opsForList().leftPop(QUEUE_KEY);
    }

    public void saveResult(JobResult result) {
        String key = RESULT_KEY + result.getId();
        resultTemplate.opsForValue().set(key, result);
        resultTemplate.opsForZSet().add(SET_KEY + result.getUserId(), result, result.getProcessedAt().toEpochMilli());
        cacheTemplate.opsForValue().set(CACHE_KEY + result.getUserId(), result.getProcessedContent());
    }

    public List<JobResult> getJobHistory(String userId) {
        Set<JobResult> results = resultTemplate.opsForZSet().range(SET_KEY + userId, 0, -1);
        return results != null ? new ArrayList<>(results) : new ArrayList<>();
    }

    public String getLatestResult(String userId) {
        return cacheTemplate.opsForValue().get(CACHE_KEY + userId);
    }
} 