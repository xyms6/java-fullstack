package com.example.cadastrousuarios.service;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cadastrousuarios.dto.LoginRequest;
import com.example.cadastrousuarios.dto.LoginResponse;
import com.example.cadastrousuarios.dto.RecuperacaoSenhaDTO;
import com.example.cadastrousuarios.dto.UsuarioDTO;
import com.example.cadastrousuarios.exception.NaoAutorizadoException;
import com.example.cadastrousuarios.exception.RecursoNaoEncontradoException;
import com.example.cadastrousuarios.exception.ServicoException;
import com.example.cadastrousuarios.exception.ValidacaoException;
import com.example.cadastrousuarios.model.Usuario;
import com.example.cadastrousuarios.repository.UsuarioRepository;
import com.example.cadastrousuarios.security.CustomUserDetailsService;
import com.example.cadastrousuarios.security.JwtTokenProvider;

@Service
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UsuarioRepository usuarioRepository,
                     PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager,
                     JwtTokenProvider tokenProvider,
                     EmailService emailService,
                     CustomUserDetailsService customUserDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public void registrar(UsuarioDTO usuarioDTO) throws Exception {
        validarRegistro(usuarioDTO);
        
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail().toLowerCase());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        
        usuarioRepository.save(usuario);
    }

    private void validarRegistro(UsuarioDTO usuarioDTO) throws Exception {
        if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmacaoSenha())) {
            throw new Exception("Senha e confirmação de senha não coincidem");
        }
        
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail().toLowerCase())) {
            throw new Exception("Email já está em uso");
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = autenticarUsuario(loginRequest);
            String token = gerarTokenJwt(authentication);
            Usuario usuario = buscarUsuarioPorEmail(loginRequest.getEmail());
            
            return new LoginResponse(token, usuario.getEmail(), usuario.getNome());
        } catch (BadCredentialsException e) {
            throw new NaoAutorizadoException("Email ou senha inválidos");
        } catch (DisabledException e) {
            throw new NaoAutorizadoException("Usuário desabilitado");
        } catch (Exception e) {
            throw new ServicoException("Erro durante o login", e);
        }
    }

    private Authentication autenticarUsuario(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail().toLowerCase(),
                loginRequest.getSenha()
            )
        );
    }

    private String gerarTokenJwt(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public void solicitarRecuperacaoSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        
        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacaoSenha(token);
        usuarioRepository.save(usuario);
        
        enviarEmailRecuperacao(usuario.getEmail(), token);
    }

    private void enviarEmailRecuperacao(String email, String token) {
        String assunto = "Recuperação de Senha";
        String mensagem = String.format(
            "Use o seguinte token para redefinir sua senha: %s%n%n" +
            "Ou acesse o link: http://seusite.com/recuperar-senha?token=%s",
            token, token
        );
        
        emailService.enviarEmail(email, assunto, mensagem);
    }

    public void alterarSenha(RecuperacaoSenhaDTO recuperacaoSenhaDTO) {
        Usuario usuario = validarTokenRecuperacao(recuperacaoSenhaDTO.getToken());
        
        usuario.setSenha(passwordEncoder.encode(recuperacaoSenhaDTO.getNovaSenha()));
        usuario.setTokenRecuperacaoSenha(null);
        usuarioRepository.save(usuario);
    }

    private Usuario validarTokenRecuperacao(String token) {
        return usuarioRepository.findByTokenRecuperacaoSenha(token)
                .orElseThrow(() -> new ValidacaoException("Token inválido ou expirado"));
    }
}