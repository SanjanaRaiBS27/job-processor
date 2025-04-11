package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.model.JobRequest;
import com.example.model.JobResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public RedisTemplate<String, JobRequest> jobQueueTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, JobRequest> redis = new RedisTemplate<>();
        redis.setConnectionFactory(factory);
        
        // Create JSON serializer for JobRequest
        Jackson2JsonRedisSerializer<JobRequest> serializer = new Jackson2JsonRedisSerializer<>(JobRequest.class);
        serializer.setObjectMapper(objectMapper);
        
        // Set serializers
        redis.setKeySerializer(new StringRedisSerializer());
        redis.setValueSerializer(serializer);
        redis.setHashKeySerializer(new StringRedisSerializer());
        redis.setHashValueSerializer(serializer);
        
        redis.afterPropertiesSet();
        return redis;
    }

    @Bean
    public RedisTemplate<String, JobResult> jobResultTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, JobResult> redis = new RedisTemplate<>();
        redis.setConnectionFactory(factory);
        
        // Create JSON serializer for JobResult
        Jackson2JsonRedisSerializer<JobResult> serializer = new Jackson2JsonRedisSerializer<>(JobResult.class);
        serializer.setObjectMapper(objectMapper);
        
        // Set serializers
        redis.setKeySerializer(new StringRedisSerializer());
        redis.setValueSerializer(serializer);
        redis.setHashKeySerializer(new StringRedisSerializer());
        redis.setHashValueSerializer(serializer);
        
        redis.afterPropertiesSet();
        return redis;
    }

    @Bean
    public RedisTemplate<String, String> cacheTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redis = new RedisTemplate<>();
        redis.setConnectionFactory(factory);
        
        // Use StringRedisSerializer for both keys and values
        StringRedisSerializer serializer = new StringRedisSerializer();
        redis.setKeySerializer(serializer);
        redis.setValueSerializer(serializer);
        redis.setHashKeySerializer(serializer);
        redis.setHashValueSerializer(serializer);
        
        redis.afterPropertiesSet();
        return redis;
    }
}