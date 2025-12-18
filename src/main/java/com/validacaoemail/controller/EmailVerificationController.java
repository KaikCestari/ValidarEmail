package com.validacaoemail.controller;

import com.validacaoemail.dto.EmailVerifyRequest;
import com.validacaoemail.dto.EmailVerifyResponse;
import com.validacaoemail.service.EmailValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Email Verification", description = "API pública para verificação de emails (requer API Key)")
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailValidationService emailValidationService;

    @Operation(
            summary = "Verifica email",
            description = "Verifica se um email é válido, bloqueando emails descartáveis. Requer API Key no header X-API-KEY.",
            security = @SecurityRequirement(name = "ApiKeyAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Verificação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = EmailVerifyResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "API Key inválida ou ausente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Rate limit excedido",
                    content = @Content
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<EmailVerifyResponse> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {
        Boolean useExternalCheck = request.getUseExternalCheck() != null ? request.getUseExternalCheck() : false;
        EmailVerifyResponse response = emailValidationService.verifyEmail(request.getEmail(), useExternalCheck);
        return ResponseEntity.ok(response);
    }
}

