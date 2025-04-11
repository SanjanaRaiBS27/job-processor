package com.example.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("content")
    private String content;

    // Default constructor for Jackson
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