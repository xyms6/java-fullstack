package com.example.cadastrousuarios.exception;

public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException(String message) {
        super(message);
    }
}