package com.validarEmail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response da verificação de email")
public class EmailVerifyResponse {

    @Schema(description = "Se o email é permitido", example = "true")
    private Boolean allowed;

    @Schema(description = "Email verificado", example = "usuario@exemplo.com")
    private String email;

    @Schema(description = "Domínio do email", example = "exemplo.com")
    private String domain;

    @Schema(description = "Se é email descartável", example = "false")
    private Boolean isDisposable;

    @Schema(description = "Se foi verificado externamente", example = "true")
    private Boolean externalChecked;

    @Schema(description = "Provedor externo usado", example = "HUNTER")
    private String externalProvider;

    @Schema(description = "Motivo da decisão", example = "VALID_EMAIL", 
            allowableValues = {"INVALID_FORMAT", "DISPOSABLE_DOMAIN", "EXTERNAL_PROVIDER_REJECTED", "EXTERNAL_PROVIDER_FAILED", "VALID_EMAIL"})
    private String reason;
}
