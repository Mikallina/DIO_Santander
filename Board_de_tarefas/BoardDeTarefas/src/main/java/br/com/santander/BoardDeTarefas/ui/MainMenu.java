package br.com.santander.BoardDeTarefas.ui;

import br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnEntity;
import br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnKindEnum;
import br.com.santander.BoardDeTarefas.persistence.entity.BoardEntity;
import br.com.santander.BoardDeTarefas.services.BoardQueryService;
import br.com.santander.BoardDeTarefas.services.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnKindEnum.*;

import static br.com.santander.BoardDeTarefas.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {

    private static final int OPTION_CREATE = 1;
    private static final int OPTION_SELECT = 2;
    private static final int OPTION_DELETE = 3;
    private static final int OPTION_EXIT = 4;

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards!");

        while (true) {
            showMenu();
            Integer option = readInt("Escolha uma opção:");

            if (option == null) {
                System.out.println("Entrada inválida. Tente novamente.");
                continue;
            }

            switch (option) {
                case OPTION_CREATE -> createBoard();
                case OPTION_SELECT -> selectBoard();
                case OPTION_DELETE -> deleteBoard();
                case OPTION_EXIT -> {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\nMenu principal:");
        System.out.println(OPTION_CREATE + " - Criar um novo board");
        System.out.println(OPTION_SELECT + " - Selecionar um board existente");
        System.out.println(OPTION_DELETE + " - Excluir um board");
        System.out.println(OPTION_EXIT + " - Sair");
    }

    private void createBoard() throws SQLException {
        BoardEntity board = new BoardEntity();
        String boardName = readString("Informe o nome do seu board:");
        if (boardName == null || boardName.isBlank()) {
            System.out.println("Nome inválido.");
            return;
        }
        board.setName(boardName);

        Integer additionalColumns = readInt("Seu board terá colunas além das 3 padrões? Informe quantas (0 para nenhuma):");
        if (additionalColumns == null || additionalColumns < 0) {
            System.out.println("Número inválido de colunas adicionais.");
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
            System.out.println("Board criado com sucesso!");
        }
    }

    private void selectBoard() throws SQLException {
        Long id = readLong("Informe o id do board que deseja selecionar:");
        if (id == null) {
            System.out.println("Id inválido.");
            return;
        }

        try (var connection = getConnection()) {
            BoardQueryService queryService = new BoardQueryService(connection);
            Optional<BoardEntity> boardOpt = queryService.findById(id);
            if (boardOpt.isPresent()) {
                new BoardMenu(boardOpt.get()).execute();
            } else {
                System.out.printf("Não foi encontrado um board com id %d\n", id);
            }
        }
    }

    private void deleteBoard() throws SQLException {
        Long id = readLong("Informe o id do board que será excluído:");
        if (id == null) {
            System.out.println("Id inválido.");
            return;
        }

        try (var connection = getConnection()) {
            BoardService service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O board %d foi excluído com sucesso!\n", id);
            } else {
                System.out.printf("Não foi encontrado um board com id %d\n", id);
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
        System.out.println(prompt);
        try {
            String line = scanner.next();
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long readLong(String prompt) {
        System.out.println(prompt);
        try {
            String line = scanner.next();
            return Long.parseLong(line.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        String input = scanner.next();
        return input.isBlank() ? null : input.trim();
    }
}
