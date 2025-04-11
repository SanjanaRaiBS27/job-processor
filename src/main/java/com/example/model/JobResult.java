package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;
import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("job_result")
public class JobResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("originalContent")
    private String originalContent;

    @JsonProperty("processedContent")
    private String processedContent;

    @JsonProperty("processedAt")
    private LocalDateTime processedAt;

    // Default constructor for Jackson
    public JobResult() {}

    public JobResult(String userId, String originalContent, String processedContent) {
        this.userId = userId;
        this.originalContent = originalContent;
        this.processedContent = processedContent;
        this.processedAt = LocalDateTime.now();
        this.id = userId + ":" + processedAt;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOriginalContent() { return originalContent; }
    public void setOriginalContent(String originalContent) { this.originalContent = originalContent; }

    public String getProcessedContent() { return processedContent; }
    public void setProcessedContent(String processedContent) { this.processedContent = processedContent; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
} 