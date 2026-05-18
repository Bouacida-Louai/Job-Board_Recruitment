package com.hirehub.companyservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String industry;
    private String website;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
