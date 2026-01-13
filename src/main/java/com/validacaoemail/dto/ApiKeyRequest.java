package com.validacaoemail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para criar API Key")
public class ApiKeyRequest {

    @NotBlank(message = "Nome não pode ser vazio")
    @Schema(description = "Nome da API Key", example = "Minha API Key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
