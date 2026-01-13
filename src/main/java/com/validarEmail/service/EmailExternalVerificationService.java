package com.validarEmail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailExternalVerificationService {

    private static final String HUNTER_API_URL = "https://api.hunter.io/v2/email-verifier";

    @Value("${hunter.api.key:}")
    private String hunterApiKey;

    @Value("${hunter.enabled:false}")
    private Boolean hunterEnabled;

    private final RestClient.Builder restClientBuilder;

    public Optional<HunterResponse> verifyEmail(String email) {
        String apiKey = normalizeApiKey();
        boolean apiKeyBlank = apiKey.isBlank();
        log.info("Hunter config - enabled={}, apiKeyBlank={}", hunterEnabled, apiKeyBlank);
        if (!hunterEnabled || apiKeyBlank) {
            log.debug("Hunter API is disabled or not configured");
            return Optional.empty();
        }

        try {
            RestClient restClient = restClientBuilder.build();
            HunterResponse response = restClient.get()
                    .uri(HUNTER_API_URL, uriBuilder -> uriBuilder
                            .queryParam("email", email)
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .body(HunterResponse.class);

            return Optional.ofNullable(response);
        } catch (RestClientResponseException e) {
            log.warn("Error calling Hunter API: status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Error calling Hunter API: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean isHunterConfigured() {
        String apiKey = normalizeApiKey();
        return Boolean.TRUE.equals(hunterEnabled) && !apiKey.isBlank();
    }

    private String normalizeApiKey() {
        if (hunterApiKey == null) {
            return "";
        }
        String trimmed = hunterApiKey.trim();
        if ((trimmed.startsWith("\"") && trimmed.endsWith("\""))
                || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }

    public boolean isDisposableByHunter(HunterResponse response) {
        if (response == null || response.getData() == null) {
            return false;
        }
        return "disposable".equalsIgnoreCase(response.getData().getResult());
    }

    public boolean isValidByHunter(HunterResponse response) {
        if (response == null || response.getData() == null) {
            return false;
        }
        return "deliverable".equalsIgnoreCase(response.getData().getResult());
    }

    public static class HunterResponse {
        private HunterData data;

        public HunterData getData() {
            return data;
        }

        public void setData(HunterData data) {
            this.data = data;
        }
    }

    public static class HunterData {
        private String result;
        private String email;
        private String domain;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }
}
