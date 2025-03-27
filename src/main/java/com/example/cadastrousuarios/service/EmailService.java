package com.example.cadastrousuarios.service;

import org.slf4j.*;
import org.springframework.stereotype.*;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        // Em produção, implementar envio de email real
        logger.info("Simulando envio de email:");
        logger.info("Destinatário: {}", destinatario);
        logger.info("Assunto: {}", assunto);
        logger.info("Mensagem: {}", mensagem);
    }
}