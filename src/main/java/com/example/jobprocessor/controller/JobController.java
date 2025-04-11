package com.example.jobprocessor.controller;

import com.example.jobprocessor.dto.JobRequest;
import com.example.jobprocessor.service.JobQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobQueueService jobQueueService;

    @PostMapping
    public String createJob(@RequestBody JobRequest request) {
        jobQueueService.push(request.getData());
        return "Job submitted!";
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<String>> getJobHistory(@PathVariable String userId) {
        try {
            List<String> history = jobQueueService.getJobHistory(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
