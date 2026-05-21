package com.hirehub.jobservice.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostedEvent {
    private Long jobId;
    private Long companyId;
    private String title;
    private String skills;
    private String location;
}
