package com.validacaoemail.service;

import com.validacaoemail.entity.ApiKey;
import com.validacaoemail.repository.ApiKeyRepository;
import com.validacaoemail.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final HashUtil hashUtil;

    public Optional<ApiKey> findByKeyHash(String keyHash) {
        return apiKeyRepository.findByKeyHashAndActiveTrue(keyHash);
    }

    public boolean isValidApiKey(String apiKey) {
        String hash = hashUtil.hashApiKey(apiKey);
        return apiKeyRepository.findByKeyHashAndActiveTrue(hash).isPresent();
    }

    @Transactional
    public ApiKeyCreateResult createApiKey(String name) {
        String apiKey = hashUtil.generateApiKey();
        String keyHash = hashUtil.hashApiKey(apiKey);

        ApiKey entity = ApiKey.builder()
                .name(name)
                .keyHash(keyHash)
                .active(true)
                .build();

        ApiKey saved = apiKeyRepository.save(entity);
        return new ApiKeyCreateResult(saved, apiKey);
    }

    public List<ApiKey> findAll() {
        return apiKeyRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        apiKeyRepository.deleteById(id);
    }

    public Optional<ApiKey> findById(Long id) {
        return apiKeyRepository.findById(id);
    }

    public static class ApiKeyCreateResult {
        private final ApiKey apiKey;
        private final String originalKey;

        public ApiKeyCreateResult(ApiKey apiKey, String originalKey) {
            this.apiKey = apiKey;
            this.originalKey = originalKey;
        }

        public ApiKey getApiKey() {
            return apiKey;
        }

        public String getOriginalKey() {
            return originalKey;
        }
    }
}
