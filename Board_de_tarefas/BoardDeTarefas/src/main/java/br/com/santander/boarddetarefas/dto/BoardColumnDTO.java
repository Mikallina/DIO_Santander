package br.com.santander.boarddetarefas.dto;

import br.com.santander.boarddetarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(
        Long id,
        String name,
        BoardColumnKindEnum kind,
        int cardsAmount
) {}