package com.example.model;

public class JobRequest {
    private String userId;
    private String content;

    public JobRequest() {}

    public JobRequest(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}