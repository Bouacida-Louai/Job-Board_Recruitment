package com.hirehub.jobservice.client;

public class CompanyClientFallback  implements CompanyClient {

    @Override
    public Boolean existsById(Long id) {
        // Fallback logic, e.g., return false or throw an exception
        return false;
    }

}
