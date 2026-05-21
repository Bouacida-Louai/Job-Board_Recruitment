package com.hirehub.jobservice.dto;


import com.hirehub.jobservice.entity.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;

    // Comma-separated: "Java,Spring Boot,Docker"
    private String skills;
}