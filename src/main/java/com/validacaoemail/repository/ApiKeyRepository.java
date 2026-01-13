package com.validacaoemail.repository;

import com.validacaoemail.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByKeyHash(String keyHash);
    Optional<ApiKey> findByKeyHashAndActiveTrue(String keyHash);
}
