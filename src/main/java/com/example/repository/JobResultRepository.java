package com.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.model.JobResult;

@Repository
public class JobResultRepository {
    private static final String KEY_PREFIX = "job_result:";
    private static final String SET_PREFIX = "job_result_set:";

    @Autowired
    private RedisTemplate<String, JobResult> redisTemplate;

    public void save(JobResult jobResult) {
        String key = KEY_PREFIX + jobResult.getId();
        redisTemplate.opsForValue().set(key, jobResult);
        redisTemplate.opsForZSet().add(SET_PREFIX + jobResult.getUserId(), jobResult, jobResult.getProcessedAt().toEpochSecond(java.time.ZoneOffset.UTC));
    }

    public List<JobResult> findByUserId(String userId) {
        Set<JobResult> jobResults = redisTemplate.opsForZSet().range(SET_PREFIX + userId, 0, -1);
        return jobResults != null ? new ArrayList<>(jobResults) : new ArrayList<>();
    }

    public JobResult findFirstByUserIdOrderByProcessedAtDesc(String userId) {
        Set<JobResult> jobResults = redisTemplate.opsForZSet().reverseRange(SET_PREFIX + userId, 0, 0);
        return jobResults != null && !jobResults.isEmpty() ? jobResults.iterator().next() : null;
    }
} 