package br.com.santander.santander.controller.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleMensagemIllegalException(IllegalArgumentException mensagemIllegalException){
        return new ResponseEntity<>(mensagemIllegalException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleMensagemNoSuchException(NoSuchElementException mensagemNoSuchException){
        return new ResponseEntity<>("ID Not found", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(NoSuchElementException mensagemErroInesperado){
        var mensagem = "Erro Inesperado, veja os logs";
        logger.error(mensagem, mensagemErroInesperado);
        return new ResponseEntity<>(mensagem, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
