package com.example.cadastrousuarios.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cadastrousuarios.dto.LoginRequest;
import com.example.cadastrousuarios.dto.LoginResponse;
import com.example.cadastrousuarios.dto.RecuperacaoSenhaDTO;
import com.example.cadastrousuarios.dto.UsuarioDTO;
import com.example.cadastrousuarios.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody UsuarioDTO usuarioDTO) throws Exception {
        authService.registrar(usuarioDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> solicitarRecuperacaoSenha(@RequestParam String email) {
        try {
            authService.solicitarRecuperacaoSenha(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
            		);
        }
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<?> alterarSenha(@RequestBody RecuperacaoSenhaDTO recuperacaoSenhaDTO) {
        try {
            authService.alterarSenha(recuperacaoSenhaDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
            );
        }
}
    
}