package br.com.santander.BoardDeTarefas.dto;

import br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(
        Long id,
        String name,
        BoardColumnKindEnum kind,
        int cardsAmount
) {}