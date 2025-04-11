package com.example.model;

import java.time.Instant;

public class JobResult {
    private String id;
    private String userId;
    private String originalContent;
    private String processedContent;
    private Instant processedAt;

    public JobResult() {}

    public JobResult(String userId, String content) {
        this.userId = userId;
        this.originalContent = content;
        this.processedContent = "Processed: " + content.toUpperCase();
        this.processedAt = Instant.now();
        this.id = userId + ":" + this.processedAt.toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOriginalContent() { return originalContent; }
    public void setOriginalContent(String originalContent) { this.originalContent = originalContent; }
    public String getProcessedContent() { return processedContent; }
    public void setProcessedContent(String processedContent) { this.processedContent = processedContent; }
    public Instant getProcessedAt() { return processedAt; }
    public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
} 