package br.com.santander.BoardDeTarefas.exceptions;

public class CardBlockedException extends RuntimeException{

    public CardBlockedException(final String message) {
        super(message);
    }
}
