package com.example.cadastrousuarios.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cadastrousuarios.exception.ConflitoException;
import com.example.cadastrousuarios.exception.RecursoNaoEncontradoException;
import com.example.cadastrousuarios.model.Usuario;
import com.example.cadastrousuarios.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable).getContent();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public Usuario criar(Usuario usuario) {
        validarEmailUnico(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    validarEmailUnico(usuarioAtualizado.getEmail(), id);
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        
        usuarioRepository.delete(usuario);
    }

    private void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email.toLowerCase())) {
            throw new ConflitoException("Email já está em uso");
        }
    }

    private void validarEmailUnico(String email, Long id) {
        usuarioRepository.findByEmailAndIdNot(email.toLowerCase(), id)
                .ifPresent(u -> {
                    throw new ConflitoException("Email já está em uso por outro usuário");
                });
    }
}