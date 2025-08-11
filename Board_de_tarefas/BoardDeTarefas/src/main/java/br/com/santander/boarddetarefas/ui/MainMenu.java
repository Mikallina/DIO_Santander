package br.com.santander.boarddetarefas.ui;

import br.com.santander.boarddetarefas.persistence.entity.BoardColumnEntity;
import br.com.santander.boarddetarefas.persistence.entity.BoardColumnKindEnum;
import br.com.santander.boarddetarefas.persistence.entity.BoardEntity;
import br.com.santander.boarddetarefas.services.BoardQueryService;
import br.com.santander.boarddetarefas.services.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static br.com.santander.boarddetarefas.persistence.entity.BoardColumnKindEnum.*;

import static br.com.santander.boarddetarefas.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {

    private static final int OPTION_CREATE = 1;
    private static final int OPTION_SELECT = 2;
    private static final int OPTION_DELETE = 3;
    private static final int OPTION_EXIT = 4;

    private static final Logger logger = LoggerFactory.getLogger(MainMenu.class);

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() throws SQLException {
        logger.info("Bem vindo ao gerenciador de boards!");

        boolean running = true;
        while (running) {
            showMenu();
            Integer option = readInt("Escolha uma opção:");

            if (option == null) {
                logger.warn("Entrada inválida. Tente novamente.");
                continue;
            }

            switch (option) {
                case OPTION_CREATE -> createBoard();
                case OPTION_SELECT -> selectBoard();
                case OPTION_DELETE -> deleteBoard();
                case OPTION_EXIT -> {
                    logger.info("Saindo do gerenciador...");
                    running = false;  // encerra o loop corretamente
                }
                default -> logger.warn("Opção inválida, tente novamente.");
            }
        }
        // Aqui pode-se fazer limpeza ou fechamento de recursos, se necessário
    }

    private void showMenu() {
        logger.info("\nMenu principal:");
        logger.info("1 - Criar um novo board");
        logger.info("2 - Selecionar um board existente");
        logger.info("3 - Excluir um board");
        logger.info("4 - Sair");
    }

    private void createBoard() throws SQLException {
        BoardEntity board = new BoardEntity();
        String boardName = readString("Informe o nome do seu board:");
        if (boardName == null || boardName.isBlank()) {
           logger.error("Nome inválido.");
            return;
        }
        board.setName(boardName);

        Integer additionalColumns = readInt("Seu board terá colunas além das 3 padrões? Informe quantas (0 para nenhuma):");
        if (additionalColumns == null || additionalColumns < 0) {
           logger.error("Número inválido de colunas adicionais.");
            return;
        }

        List<BoardColumnEntity> columns = new ArrayList<>();

        // Coluna Inicial (padrão)
        String initialName = readString("Informe o nome da coluna inicial do board:");
        columns.add(createColumn(initialName, INITIAL, 0));

        // Colunas adicionais do tipo PENDING
        for (int i = 0; i < additionalColumns; i++) {
            String pendingName = readString("Informe o nome da coluna de tarefa pendente " + (i + 1) + ":");
            columns.add(createColumn(pendingName, PENDING, i + 1));
        }

        // Coluna Final (padrão)
        String finalName = readString("Informe o nome da coluna final:");
        columns.add(createColumn(finalName, FINAL, additionalColumns + 1));

        // Coluna Cancelamento (padrão)
        String cancelName = readString("Informe o nome da coluna de cancelamento:");
        columns.add(createColumn(cancelName, CANCEL, additionalColumns + 2));

        board.setBoardColumns(columns);

        try (var connection = getConnection()) {
            BoardService service = new BoardService(connection);
            service.insert(board);
           logger.info("Board criado com sucesso!");
        }
    }

    private void selectBoard() throws SQLException {
        Long id = readLong("Informe o id do board que deseja selecionar:");
        if (id == null) {
            logger.error("Id inválido.");
            return;
        }

        try (var connection = getConnection()) {
            BoardQueryService queryService = new BoardQueryService(connection);
            Optional<BoardEntity> boardOpt = queryService.findById(id);
            if (boardOpt.isPresent()) {
                new BoardMenu(boardOpt.get()).execute();
            } else {
                logger.warn("Não foi encontrado um board com id {}", id);
            }
        }
    }

    private void deleteBoard() throws SQLException {
        Long id = readLong("Informe o id do board que será excluído:");
        if (id == null) {
            logger.warn("Id inválido.");
            return;
        }

        try (var connection = getConnection()) {
            BoardService service = new BoardService(connection);
            if (service.delete(id)) {
               logger.info("O board %d foi excluído com sucesso {}", id);
            } else {
               logger.error("Não foi encontrado um board com id {}", id);
            }
        }
    }

    private BoardColumnEntity createColumn(String name, BoardColumnKindEnum kind, int order) {
        BoardColumnEntity column = new BoardColumnEntity();
        column.setName(name);
        column.setKind(kind);
        column.setOrder(order);
        return column;
    }

    private Integer readInt(String prompt) {
        logger.info(prompt);
        try {
            String line = scanner.next();
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long readLong(String prompt) {
        logger.info(prompt);
        try {
            String line = scanner.next();
            return Long.parseLong(line.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String readString(String prompt) {
       logger.info(prompt);
        String input = scanner.next();
        return input.isBlank() ? null : input.trim();
    }
}
