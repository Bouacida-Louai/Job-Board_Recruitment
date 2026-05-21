package com.hirehub.jobservice.repository;

import com.hirehub.jobservice.entity.Job;
import com.hirehub.jobservice.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompanyId(Long companyId);
    List<Job> findByStatus(JobStatus status);
    List<Job> findByCompanyIdAndStatus(Long companyId, JobStatus status);

}
