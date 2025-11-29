package com.validacaoemail.repository;

import com.validacaoemail.entity.DisposableDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisposableDomainRepository extends JpaRepository<DisposableDomain, Long> {
    Optional<DisposableDomain> findByDomainIgnoreCase(String domain);
    boolean existsByDomainIgnoreCase(String domain);
}

