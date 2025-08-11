package br.com.santander.BoardDeTarefas.exceptions;

public class CardFinishedException extends RuntimeException{

    public CardFinishedException(final String message) {
        super(message);
    }
}