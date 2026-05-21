package com.hirehub.jobservice.controller;

import com.hirehub.jobservice.dto.JobRequest;
import com.hirehub.jobservice.dto.JobResponse;
import com.hirehub.jobservice.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody JobRequest request) {

        if (!role.equals("ROLE_COMPANY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // userId here is the userId — we need to get their companyId
        // For simplicity we use userId as companyId lookup key
        // job-service calls company-service to validate
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobService.createJob(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllOpenJobs() {
        return ResponseEntity.ok(jobService.getAllOpenJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobResponse>> getJobsByCompany(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<JobResponse> closeJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("ROLE_COMPANY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(jobService.closeJob(id, userId));
    }
}