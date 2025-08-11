package br.com.santander.boarddetarefas.exceptions;

public class CardFinishedException extends RuntimeException{

    public CardFinishedException(final String message) {
        super(message);
    }
}