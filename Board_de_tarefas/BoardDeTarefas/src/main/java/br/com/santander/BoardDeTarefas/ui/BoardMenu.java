package br.com.santander.BoardDeTarefas.ui;

import br.com.santander.BoardDeTarefas.dto.BoardColumnInfoDTO;
import br.com.santander.BoardDeTarefas.persistence.entity.BoardColumnEntity;
import br.com.santander.BoardDeTarefas.persistence.entity.BoardEntity;
import br.com.santander.BoardDeTarefas.persistence.entity.CardEntity;
import br.com.santander.BoardDeTarefas.services.BoardColumnQueryService;
import br.com.santander.BoardDeTarefas.services.BoardQueryService;
import br.com.santander.BoardDeTarefas.services.CardQueryService;
import br.com.santander.BoardDeTarefas.services.CardService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


import static br.com.santander.BoardDeTarefas.persistence.config.ConnectionConfig.getConnection;

public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardEntity entity;

    public BoardMenu(BoardEntity entity) {
        this.entity = entity;
    }

    public void execute() {
        System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", entity.getId());
        int option;
        do {
            option = readMenuOption();
            try {
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> changeCardBlockStatus(true);
                    case 4 -> changeCardBlockStatus(false);
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> {
                        System.out.println("Saindo...");
                        System.exit(0);
                    }
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao acessar banco de dados: " + ex.getMessage());
            }
        } while (option != 9);
    }

    private int readMenuOption() {
        System.out.println("\nMenu:");
        System.out.println("1 - Criar um card");
        System.out.println("2 - Mover um card");
        System.out.println("3 - Bloquear um card");
        System.out.println("4 - Desbloquear um card");
        System.out.println("5 - Cancelar um card");
        System.out.println("6 - Ver board");
        System.out.println("7 - Ver coluna com cards");
        System.out.println("8 - Ver card");
        System.out.println("9 - Voltar para o menu anterior");
        System.out.println("10 - Sair");
        System.out.print("Escolha uma opção: ");

        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, informe um número.");
            scanner.nextLine(); // limpa entrada inválida
            return -1;
        }
    }

    private List<BoardColumnInfoDTO> getBoardColumnsInfo() {
        return entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
    }

    private void createCard() throws SQLException {
        var card = new CardEntity();
        System.out.print("Informe o título do card: ");
        card.setTitle(scanner.next());
        System.out.print("Informe a descrição do card: ");
        card.setDescription(scanner.next());
        card.setBoardColumn(entity.getInitialColumn());

        try (var connection = getConnection()) {
            new CardService(connection).create(card);
            System.out.println("Card criado com sucesso!");
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.print("Informe o id do card que deseja mover para a próxima coluna: ");
        var cardId = readLongInput();
        if (cardId == null) return;

        var boardColumnsInfo = getBoardColumnsInfo();

        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
            System.out.println("Card movido com sucesso!");
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void changeCardBlockStatus(boolean block) throws SQLException {
        System.out.printf("Informe o id do card que será %s:\n", block ? "bloqueado" : "desbloqueado");
        var cardId = readLongInput();
        if (cardId == null) return;

        System.out.printf("Informe o motivo do %s do card:\n", block ? "bloqueio" : "desbloqueio");
        var reason = scanner.next();

        try (var connection = getConnection()) {
            var cardService = new CardService(connection);
            if (block) {
                var boardColumnsInfo = getBoardColumnsInfo();
                cardService.block(cardId, reason, boardColumnsInfo);
            } else {
                cardService.unblock(cardId, reason);
            }
            System.out.printf("Card %s com sucesso!\n", block ? "bloqueado" : "desbloqueado");
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }


    private void cancelCard() throws SQLException {
        System.out.print("Informe o id do card que deseja mover para a coluna de cancelamento: ");
        var cardId = readLongInput();
        if (cardId == null) return;

        var cancelColumn = entity.getCancelColumn();

        try (var connection = getConnection()) {
            new CardService(connection).cancel(cardId, cancelColumn.getId(), getBoardColumnsInfo());
            System.out.println("Card cancelado com sucesso!");
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresentOrElse(b -> {
                System.out.printf("Board [%s, %s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                        System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            }, () -> System.out.println("Board não encontrado"));
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = entity.getBoardColumns().stream()
                .map(BoardColumnEntity::getId)
                .toList();

        Long selectedColumnId;
        do {
            System.out.printf("Escolha uma coluna do board %s pelo id\n", entity.getName());
            entity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));

            selectedColumnId = readLongInput();
            if (selectedColumnId == null) return;

            if (!columnsIds.contains(selectedColumnId)) {
                System.out.println("Id inválido, tente novamente.");
            }
        } while (!columnsIds.contains(selectedColumnId));

        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumnId);
            column.ifPresentOrElse(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s\n",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
            }, () -> System.out.println("Coluna não encontrada."));
        }
    }

    private void showCard() throws SQLException {
        System.out.print("Informe o id do card que deseja visualizar: ");
        var selectedCardId = readLongInput();
        if (selectedCardId == null) return;

        try (var connection = getConnection()) {
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s.\n", c.id(), c.title());
                                System.out.printf("Descrição: %s\n", c.description());
                                System.out.println(c.blocked() ?
                                        "Está bloqueado. Motivo: " + c.blockReason() :
                                        "Não está bloqueado");
                                System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                                System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("Não existe um card com o id %s\n", selectedCardId));
        }
    }

    private Long readLongInput() {
        try {
            return scanner.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, informe um número válido.");
            scanner.nextLine(); // limpa a entrada inválida
            return null;
        }
    }
}