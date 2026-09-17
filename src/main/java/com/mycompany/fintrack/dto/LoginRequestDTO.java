package com.mycompany.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciais de login")
public class LoginRequestDTO {

    @NotBlank
    @Email
    @Schema(description = "Email do usuário", example = "joao@email.com")
    private String email;

    @NotBlank
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;
}