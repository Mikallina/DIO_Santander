package br.com.santander.BoardDeTarefas.services;

import br.com.santander.BoardDeTarefas.persistence.dao.BoardColumnDAO;
import br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

import static br.com.santander.BoardDeTarefas.persistence.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        var dao = new BoardColumnDAO(connection);
        return dao.findById(id);
    }


}
