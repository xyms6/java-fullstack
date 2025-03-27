package com.example.cadastrousuarios.repository;

import com.example.cadastrousuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTokenRecuperacaoSenha(String token);
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmailAndIdNot(String email, Long id);
}