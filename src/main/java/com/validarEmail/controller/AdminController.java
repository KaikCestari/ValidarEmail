package com.validarEmail.controller;

import com.validarEmail.dto.ApiKeyRequest;
import com.validarEmail.dto.ApiKeyResponse;
import com.validarEmail.dto.DisposableDomainRequest;
import com.validarEmail.entity.DisposableDomain;
import com.validarEmail.repository.DisposableDomainRepository;
import com.validarEmail.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Admin", description = "Endpoints administrativos (requer JWT com ROLE_ADMIN)")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class AdminController {

    private final DisposableDomainRepository disposableDomainRepository;
    private final ApiKeyService apiKeyService;

    @Operation(summary = "Lista domínios descartáveis")
    @GetMapping("/disposable-domains")
    public ResponseEntity<List<String>> listDisposableDomains() {
        List<String> domains = disposableDomainRepository.findAll().stream()
                .map(DisposableDomain::getDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(domains);
    }

    @Operation(summary = "Adiciona domínio descartável")
    @PostMapping("/disposable-domains")
    public ResponseEntity<DisposableDomain> addDisposableDomain(@Valid @RequestBody DisposableDomainRequest request) {
        DisposableDomain domain = DisposableDomain.builder()
                .domain(request.getDomain().toLowerCase())
                .build();
        DisposableDomain saved = disposableDomainRepository.save(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Remove domínio descartável")
    @DeleteMapping("/disposable-domains/{domain}")
    public ResponseEntity<Void> removeDisposableDomain(@PathVariable String domain) {
        disposableDomainRepository.findByDomainIgnoreCase(domain)
                .ifPresent(disposableDomainRepository::delete);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista API Keys")
    @GetMapping("/api-keys")
    public ResponseEntity<List<ApiKeyResponse>> listApiKeys() {
        List<ApiKeyResponse> keys = apiKeyService.findAll().stream()
                .map(key -> ApiKeyResponse.builder()
                        .id(key.getId())
                        .name(key.getName())
                        .active(key.getActive())
                        .createdAt(key.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(keys);
    }

    @Operation(summary = "Cria nova API Key")
    @PostMapping("/api-keys")
    public ResponseEntity<ApiKeyResponse> createApiKey(@Valid @RequestBody ApiKeyRequest request) {
        ApiKeyService.ApiKeyCreateResult result = apiKeyService.createApiKey(request.getName());
        
        ApiKeyResponse response = ApiKeyResponse.builder()
                .id(result.getApiKey().getId())
                .name(result.getApiKey().getName())
                .key(result.getOriginalKey())
                .active(result.getApiKey().getActive())
                .createdAt(result.getApiKey().getCreatedAt())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Remove API Key")
    @DeleteMapping("/api-keys/{id}")
    public ResponseEntity<Void> deleteApiKey(@PathVariable Long id) {
        apiKeyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

