package com.validarEmail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailValidationResponse {

    private String email;
    private boolean isValid;
    private boolean isDisposable;
    private boolean hasValidDomain;
    private String message;
    private ValidationDetails details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ValidationDetails {
        private boolean formatValid;
        private boolean domainExists;
        private boolean hasMxRecords;
        private boolean isDisposableDomain;
    }
}

