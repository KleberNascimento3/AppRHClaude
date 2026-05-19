package com.AppRH.AppRH.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public String handleRegistroNaoEncontrado(RegistroNaoEncontradoException ex, Model model) {
        log.warn("Registro não encontrado: {}", ex.getMessage());
        model.addAttribute("erro", "Registro não encontrado: " + ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return "mensagens/acesso-negado";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        model.addAttribute("erro", "Ocorreu um erro inesperado. Tente novamente.");
        return "redirect:/";
    }
}
