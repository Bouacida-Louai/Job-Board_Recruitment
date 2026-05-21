package com.hirehub.jobservice.service;


import com.hirehub.jobservice.client.CompanyClient;
import com.hirehub.jobservice.dto.JobRequest;
import com.hirehub.jobservice.dto.JobResponse;
import com.hirehub.jobservice.entity.Job;
import com.hirehub.jobservice.entity.JobStatus;
import com.hirehub.jobservice.event.JobPostedEvent;
import com.hirehub.jobservice.repository.JobRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @CircuitBreaker(name = "company-service", fallbackMethod = "createJobFallback")
    public JobResponse createJob(Long companyId, JobRequest request) {
        // Validate company exists via company-service
        Boolean exists = companyClient.existsById(companyId);
        if (!exists) {
            throw new IllegalArgumentException(
                    "Company not found or unavailable");
        }

        Job job = Job.builder()
                .companyId(companyId)
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .skills(request.getSkills())
                .build();

        Job saved = jobRepository.save(job);

        // Fire Kafka event
        JobPostedEvent event = JobPostedEvent.builder()
                .jobId(saved.getId())
                .companyId(saved.getCompanyId())
                .title(saved.getTitle())
                .skills(saved.getSkills())
                .location(saved.getLocation())
                .build();

        kafkaTemplate.send("job.posted", event);
        log.info("Job posted event sent for jobId: {}", saved.getId());

        return toResponse(saved);
    }

    // Fallback — called when circuit breaker opens
    public JobResponse createJobFallback(Long companyId,
                                         JobRequest request, Exception e) {
        log.error("Circuit breaker triggered for company-service: {}",
                e.getMessage());
        throw new IllegalStateException(
                "Company service is unavailable. Please try again later.");
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return toResponse(job);
    }

    public List<JobResponse> getAllOpenJobs() {
        return jobRepository.findByStatus(JobStatus.OPEN)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public JobResponse closeJob(Long jobId, Long companyId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompanyId().equals(companyId)) {
            throw new IllegalArgumentException(
                    "You can only close your own jobs");
        }

        job.setStatus(JobStatus.CLOSED);
        Job saved = jobRepository.save(job);

        kafkaTemplate.send("job.closed", jobId.toString());
        log.info("Job closed event sent for jobId: {}", jobId);

        return toResponse(saved);
    }

    private JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .companyId(job.getCompanyId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .status(job.getStatus())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .skills(job.getSkills())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}