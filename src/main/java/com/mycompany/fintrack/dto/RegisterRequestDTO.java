package com.mycompany.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para registro de novo usuário")
public class RegisterRequestDTO {

    @NotBlank
    @Schema(description = "Nome completo", example = "João Silva")
    private String nome;

    @NotBlank
    @Email
    @Schema(description = "Email", example = "joao@email.com")
    private String email;

    @NotBlank
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha", example = "123456")
    private String senha;
}