package br.com.sudoku.logic;

import br.com.sudoku.model.SudokuModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SudokuController {
    private final SudokuModel model;

    private RemoveNumbers removeNumbers;

    public SudokuController(SudokuModel model, RemoveNumbers removeNumbers) {
        this.model = model;
        this.removeNumbers = removeNumbers;

    }

    public void loadInitialBoard(DefaultTableModel modelTable) {
        SudokuGenerator generator = new SudokuGenerator();
        int[][] fullBoard = generator.generate();
        model.setSolution(copyMatrix(fullBoard));


        removeNumbers.removeNumbers(fullBoard, 30);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (fullBoard[row][col] != 0) {
                    modelTable.setValueAt(String.valueOf(fullBoard[row][col]), row, col);
                    model.setLocked(row, col, true);
                } else {
                    modelTable.setValueAt(null, row, col);
                    model.setLocked(row, col, false);
                }
            }
        }
    }

    public void restartGame(DefaultTableModel modelTable) {
        model.reset();
        loadInitialBoard(modelTable);
    }

    public boolean checkGame(JTable table, JPanel mainPanel) {
        boolean allFilled = true;
        boolean allCorrect = true;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Object value = table.getValueAt(row, col);
                if (isCellEmpty(value)) {
                    allFilled = false;
                    allCorrect = false;
                } else {
                    if (!isCellValueCorrect(value, row, col)) {
                        allCorrect = false;
                        markCellAsError(table, row, col);
                    }
                }
            }
        }

        showResultMessage(mainPanel, allFilled, allCorrect);
        return allCorrect;
    }

    private boolean isCellEmpty(Object value) {
        return value == null || value.toString().isBlank();
    }

    private boolean isCellValueCorrect(Object value, int row, int col) {
        try {
            int userValue = Integer.parseInt(value.toString());
            return userValue == model.getSolutionValue(row, col);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void markCellAsError(JTable table, int row, int col) {
        table.changeSelection(row, col, false, false);
        table.editCellAt(row, col);
        table.getEditorComponent().setBackground(new java.awt.Color(255, 200, 200));
    }

    private void showResultMessage(JPanel mainPanel, boolean allFilled, boolean allCorrect) {
        if (!allFilled) {
            JOptionPane.showMessageDialog(mainPanel, "Você ainda não preencheu todas as células.", "Incompleto", JOptionPane.WARNING_MESSAGE);
        } else if (allCorrect) {
            JOptionPane.showMessageDialog(mainPanel, "Parabéns! Você resolveu corretamente o Sudoku!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Há erros no seu Sudoku. Tente corrigir!", "Erros encontrados", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}
