package br.com.santander.BoardDeTarefas.dto;

import br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}
