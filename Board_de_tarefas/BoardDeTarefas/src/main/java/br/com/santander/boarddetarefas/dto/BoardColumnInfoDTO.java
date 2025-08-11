package br.com.santander.boarddetarefas.dto;

import br.com.santander.boarddetarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}
