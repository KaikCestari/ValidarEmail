package com.validarEmail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para adicionar domínio descartável")
public class DisposableDomainRequest {

    @NotBlank(message = "Domínio não pode ser vazio")
    @Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?)*$",
            message = "Formato de domínio inválido")
    @Schema(description = "Domínio a ser adicionado", example = "tempmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String domain;
}
