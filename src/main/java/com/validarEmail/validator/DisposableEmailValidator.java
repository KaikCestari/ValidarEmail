package com.validarEmail.validator;

import com.validarEmail.repository.DisposableDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisposableEmailValidator {

    private final DisposableDomainRepository disposableDomainRepository;

    public boolean isDisposable(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return disposableDomainRepository.existsByDomainIgnoreCase(domain);
    }
}
