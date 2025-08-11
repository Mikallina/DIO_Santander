package br.com.santander.boarddetarefas.ui;

import br.com.santander.boarddetarefas.dto.BoardColumnInfoDTO;
import br.com.santander.boarddetarefas.persistence.entity.BoardColumnEntity;
import br.com.santander.boarddetarefas.persistence.entity.BoardEntity;
import br.com.santander.boarddetarefas.persistence.entity.CardEntity;
import br.com.santander.boarddetarefas.services.BoardColumnQueryService;
import br.com.santander.boarddetarefas.services.BoardQueryService;
import br.com.santander.boarddetarefas.services.CardQueryService;
import br.com.santander.boarddetarefas.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static br.com.santander.boarddetarefas.persistence.config.ConnectionConfig.getConnection;

public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardEntity entity;
    private static final Logger logger = LoggerFactory.getLogger(BoardMenu.class);

    public BoardMenu(BoardEntity entity) {
        this.entity = entity;
    }

    public void execute() {
        logger.info("Bem vindo ao board, {} selecione a operação desejada", entity.getId());
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
                    case 9 -> logger.info("Voltando para o menu anterior");
                    case 10 -> {
                        logger.info("Saindo...");
                        System.exit(0);
                    }
                    default -> logger.error("Opção inválida, informe uma opção do menu");
                }
            } catch (SQLException ex) {
                logger.error("Erro ao acessar banco de dados: {}", ex.getMessage());
            }
        } while (option != 9);
    }

    private int readMenuOption() {
        logger.info("\nMenu:");
        logger.info("1 - Criar um card");
        logger.info("2 - Mover um card");
        logger.info("3 - Bloquear um card");
        logger.info("4 - Desbloquear um card");
        logger.info("5 - Cancelar um card");
        logger.info("6 - Ver board");
        logger.info("7 - Ver coluna com cards");
        logger.info("8 - Ver card");
        logger.info("9 - Voltar para o menu anterior");
        logger.info("10 - Sair");
        logger.info("Escolha uma opção: "); // prompt de entrada continua com System.out.print para ficar na mesma linha

        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            logger.warn("Entrada inválida! Por favor, informe um número.");
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
        logger.info("Informe o título do card: ");
        card.setTitle(scanner.next());
        logger.info("Informe a descrição do card: ");
        card.setDescription(scanner.next());
        card.setBoardColumn(entity.getInitialColumn());

        try (var connection = getConnection()) {
            new CardService(connection).create(card);
            logger.info("Card criado com sucesso!");
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        logger.info("Informe o id do card que deseja mover para a próxima coluna: ");
        var cardId = readLongInput();
        if (cardId == null) return;

        var boardColumnsInfo = getBoardColumnsInfo();

        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
            logger.info("Card movido com sucesso!");
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage());
        }
    }

    private void changeCardBlockStatus(boolean block) throws SQLException {
        logger.info("Informe o id do card que será {}", block ? "bloqueado" : "desbloqueado");
        var cardId = readLongInput();
        if (cardId == null) return;

        logger.info("Informe o motivo do card: {}", block ? "bloqueio" : "desbloqueio");
        var reason = scanner.next();

        try (var connection = getConnection()) {
            var cardService = new CardService(connection);
            if (block) {
                var boardColumnsInfo = getBoardColumnsInfo();
                cardService.block(cardId, reason, boardColumnsInfo);
            } else {
                cardService.unblock(cardId, reason);
            }
            logger.info("Card {} com sucesso!", block ? "bloqueado" : "desbloqueado");
        } catch (RuntimeException ex) {
            logger.error("Erro: {}", ex.getMessage());
        }
    }

    private void cancelCard() throws SQLException {
        logger.info("Informe o id do card que deseja mover para a coluna de cancelamento: ");
        var cardId = readLongInput();
        if (cardId == null) return;

        var cancelColumn = entity.getCancelColumn();

        try (var connection = getConnection()) {
            new CardService(connection).cancel(cardId, cancelColumn.getId(), getBoardColumnsInfo());
            logger.info("Card cancelado com sucesso!");
        } catch (RuntimeException ex) {
            logger.error("Erro: {}", ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresentOrElse(b -> {
                logger.info("Board [{} - {}]", b.id(), b.name());
                b.columns().forEach(c ->
                        logger.info("Coluna [{}] tipo: [{}] tem {} cards", c.name(), c.kind(), c.cardsAmount())
                );
            }, () -> logger.warn("Board não encontrado"));
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = entity.getBoardColumns().stream()
                .map(BoardColumnEntity::getId)
                .toList();

        Long selectedColumnId;
        do {
            logger.info("Escolha uma coluna do board {} pelo id", entity.getName());
            entity.getBoardColumns().forEach(c -> logger.info("{} - {} [{}]", c.getId(), c.getName(), c.getKind()));

            selectedColumnId = readLongInput();
            if (selectedColumnId == null) return;

            if (!columnsIds.contains(selectedColumnId)) {
                logger.warn("Id inválido, tente novamente.");
            }
        } while (!columnsIds.contains(selectedColumnId));

        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumnId);
            column.ifPresentOrElse(co -> {
                logger.info("Coluna {} tipo {}", co.getName(), co.getKind());
                co.getCards().forEach(ca -> logger.info("Card {} - {}\nDescrição: {}", ca.getId(), ca.getTitle(), ca.getDescription()));
            }, () -> logger.warn("Coluna não encontrada."));
        }
    }

    private void showCard() throws SQLException {
        logger.info("Informe o id do card que deseja visualizar: ");
        var selectedCardId = readLongInput();
        if (selectedCardId == null) return;

        try (var connection = getConnection()) {
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                logger.info("Card {} - {}.", c.id(), c.title());
                                logger.info("Descrição: {}", c.description());
                                logger.info(c.blocked() ?
                                        "Está bloqueado. Motivo: " + c.blockReason() :
                                        "Não está bloqueado");
                                logger.info("Já foi bloqueado {} vezes", c.blocksAmount());
                                logger.info("Está no momento na coluna {} - {}", c.columnId(), c.columnName());
                            },
                            () -> logger.warn("Não existe um card com o id {}", selectedCardId));
        }
    }

    private Long readLongInput() {
        try {
            return scanner.nextLong();
        } catch (InputMismatchException e) {
            logger.warn("Entrada inválida! Por favor, informe um número válido.");
            scanner.nextLine(); // limpa a entrada inválida
            return null;
        }
    }
}
