package com.validarEmail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para verificação de email")
public class EmailVerifyRequest {

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Formato de email inválido")
    @Schema(description = "Email a ser verificado", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Se deve usar verificação externa (Hunter API)", example = "true", defaultValue = "false")
    @JsonProperty("useExternalCheck")
    private Boolean useExternalCheck = false;
}
