package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobCacheService {

    @Autowired
    private RedisTemplate<String, String> cacheTemplate;

    public void cacheResult(String key, String value) {
        cacheTemplate.opsForValue().set("processed:" + key, value);
    }

    public String getCachedResult(String key) {
        return cacheTemplate.opsForValue().get("processed:" + key);
    }
}