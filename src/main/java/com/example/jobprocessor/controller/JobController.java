package com.example.jobprocessor.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobprocessor.dto.JobRequest;
import com.example.jobprocessor.service.JobQueueService;

@RestController
@RequestMapping("/api/job")
public class JobController {
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobQueueService jobQueueService;

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody JobRequest request) {
        try {
            logger.info("Received job request: {}", request.getData());
            jobQueueService.push(request.getData());
            return ResponseEntity.ok("Job submitted successfully!");
        } catch (Exception e) {
            logger.error("Error submitting job: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to submit job: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<String>> getJobHistory(@PathVariable String userId) {
        try {
            logger.info("Retrieving job history for user: {}", userId);
            List<String> history = jobQueueService.getJobHistory(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            logger.error("Error retrieving job history: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/result/{jobId}")
    public ResponseEntity<String> getProcessedResult(@PathVariable String jobId) {
        try {
            logger.info("Retrieving processed result for job: {}", jobId);
            String result = jobQueueService.getProcessedResult(jobId);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving processed result: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to retrieve result: " + e.getMessage());
        }
    }
}
