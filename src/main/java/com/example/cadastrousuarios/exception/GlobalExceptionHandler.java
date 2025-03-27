package com.example.cadastrousuarios.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {
        return handleExceptionInternal(ex, criarRespostaErro(ex), 
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<Object> handleNaoAutorizadoException(NaoAutorizadoException ex, WebRequest request) {
        return handleExceptionInternal(ex, criarRespostaErro(ex), 
            new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Object> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex, WebRequest request) {
        return handleExceptionInternal(ex, criarRespostaErro(ex), 
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<Object> handleConflitoException(ConflitoException ex, WebRequest request) {
        return handleExceptionInternal(ex, criarRespostaErro(ex), 
            new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ServicoException.class)
    public ResponseEntity<Object> handleServicoException(ServicoException ex, WebRequest request) {
        return handleExceptionInternal(ex, criarRespostaErro(ex), 
            new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, criarRespostaErro("Erro interno no servidor"), 
            new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private Map<String, String> criarRespostaErro(Exception ex) {
        return criarRespostaErro(ex.getMessage());
    }

    private Map<String, String> criarRespostaErro(String mensagem) {
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", mensagem);
        erro.put("timestamp", new Date().toString());
        return erro;
    }
}