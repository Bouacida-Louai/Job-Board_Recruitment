package com.hirehub.jobservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", fallback = CompanyClientFallback.class)
public interface CompanyClient {

    @GetMapping("api/companies/{id}/exists")
    Boolean existsById(@PathVariable Long id);

}
