package com.validarEmail.service;

import com.validarEmail.dto.EmailVerifyResponse;
import com.validarEmail.service.EmailExternalVerificationService.HunterResponse;
import com.validarEmail.validator.DisposableEmailValidator;
import com.validarEmail.validator.EmailFormatValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailValidationService {

    private final EmailFormatValidator emailFormatValidator;
    private final DisposableEmailValidator disposableEmailValidator;
    private final EmailExternalVerificationService externalVerificationService;

    public EmailVerifyResponse verifyEmail(String email, boolean useExternalCheck) {
        if (email == null || email.trim().isEmpty()) {
            return createRejectedResponse(email, null, "INVALID_FORMAT", false);
        }

        email = email.trim().toLowerCase();
        String domain = extractDomain(email);

        if (!emailFormatValidator.isValidFormat(email)) {
            return createRejectedResponse(email, domain, "INVALID_FORMAT", false);
        }

        if (disposableEmailValidator.isDisposable(email)) {
            return createRejectedResponse(email, domain, "DISPOSABLE_DOMAIN", false);
        }

        if (useExternalCheck) {
            boolean hunterConfigured = externalVerificationService.isHunterConfigured();
            Optional<HunterResponse> hunterResponse = externalVerificationService.verifyEmail(email);
            
            if (hunterResponse.isPresent()) {
                HunterResponse response = hunterResponse.get();
                
                if (externalVerificationService.isDisposableByHunter(response)) {
                    return createRejectedResponse(email, domain, "EXTERNAL_PROVIDER_REJECTED", true);
                }
                
                if (!externalVerificationService.isValidByHunter(response)) {
                    return createRejectedResponse(email, domain, "EXTERNAL_PROVIDER_REJECTED", true);
                }
                
                return createAllowedResponse(email, domain, true);
            } else {
                if (hunterConfigured) {
                    log.debug("External verification failed, using internal validation for: {}", email);
                    return createAllowedResponse(email, domain, true, "EXTERNAL_PROVIDER_FAILED");
                }
                log.debug("External verification skipped (not configured), using internal validation for: {}", email);
                return createAllowedResponse(email, domain, false);
            }
        }

        return createAllowedResponse(email, domain, false);
    }

    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf("@") + 1);
    }

    private EmailVerifyResponse createAllowedResponse(String email, String domain, boolean externalChecked) {
        return createAllowedResponse(email, domain, externalChecked, "VALID_EMAIL");
    }

    private EmailVerifyResponse createAllowedResponse(String email, String domain, boolean externalChecked, String reason) {
        return EmailVerifyResponse.builder()
                .allowed(true)
                .email(email)
                .domain(domain)
                .isDisposable(false)
                .externalChecked(externalChecked)
                .externalProvider(externalChecked ? "HUNTER" : null)
                .reason(reason)
                .build();
    }

    private EmailVerifyResponse createRejectedResponse(String email, String domain, String reason, boolean externalChecked) {
        return EmailVerifyResponse.builder()
                .allowed(false)
                .email(email)
                .domain(domain)
                .isDisposable("DISPOSABLE_DOMAIN".equals(reason))
                .externalChecked(externalChecked)
                .externalProvider(externalChecked ? "HUNTER" : null)
                .reason(reason)
                .build();
    }
}
