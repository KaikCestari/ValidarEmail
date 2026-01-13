package com.validacaoemail.service;

import com.validacaoemail.dto.EmailVerifyResponse;
import com.validacaoemail.repository.DisposableDomainRepository;
import com.validacaoemail.validator.DisposableEmailValidator;
import com.validacaoemail.validator.EmailFormatValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailValidationServiceTest {

    @Mock
    private EmailFormatValidator emailFormatValidator;

    @Mock
    private DisposableEmailValidator disposableEmailValidator;

    @Mock
    private EmailExternalVerificationService externalVerificationService;

    @InjectMocks
    private EmailValidationService emailValidationService;

    @BeforeEach
    void setUp() {
        when(emailFormatValidator.isValidFormat(anyString())).thenReturn(true);
        when(disposableEmailValidator.isDisposable(anyString())).thenReturn(false);
    }

    @Test
    void shouldRejectInvalidFormat() {
        when(emailFormatValidator.isValidFormat("invalid-email")).thenReturn(false);

        EmailVerifyResponse response = emailValidationService.verifyEmail("invalid-email", false);

        assertThat(response.getAllowed()).isFalse();
        assertThat(response.getReason()).isEqualTo("INVALID_FORMAT");
    }

    @Test
    void shouldRejectDisposableDomain() {
        when(disposableEmailValidator.isDisposable("test@tempmail.com")).thenReturn(true);

        EmailVerifyResponse response = emailValidationService.verifyEmail("test@tempmail.com", false);

        assertThat(response.getAllowed()).isFalse();
        assertThat(response.getReason()).isEqualTo("DISPOSABLE_DOMAIN");
        assertThat(response.getIsDisposable()).isTrue();
    }

    @Test
    void shouldAcceptValidEmail() {
        EmailVerifyResponse response = emailValidationService.verifyEmail("test@example.com", false);

        assertThat(response.getAllowed()).isTrue();
        assertThat(response.getReason()).isEqualTo("VALID_EMAIL");
        assertThat(response.getExternalChecked()).isFalse();
    }
}
