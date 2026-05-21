package com.hirehub.jobservice.dto;

import com.hirehub.jobservice.entity.JobStatus;
import com.hirehub.jobservice.entity.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class JobResponse {

    private Long id;
    private Long companyId;
    private String title;
    private String description;
    private String location;
    private JobType jobType;
    private JobStatus status;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String skills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}