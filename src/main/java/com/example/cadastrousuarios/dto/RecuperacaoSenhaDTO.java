package com.example.cadastrousuarios.dto;

import jakarta.validation.constraints.*;

public class RecuperacaoSenhaDTO {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotBlank(message = "O token é obrigatório")
    private String token;

    @NotBlank(message = "A nova senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String novaSenha;

    // Construtores
    public RecuperacaoSenhaDTO() {
    }

    public RecuperacaoSenhaDTO(String email, String token, String novaSenha) {
        this.email = email;
        this.token = token;
        this.novaSenha = novaSenha;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}