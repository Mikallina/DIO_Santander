package br.com.sudoku.design;

import br.com.sudoku.logic.RemoveNumbers;
import br.com.sudoku.logic.SudokuController;
import br.com.sudoku.model.SudokuModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BoardForm {
    public JPanel mainPanel;
    private JTable sudokuTable;
    private boolean draftMode = false;
    private final SudokuModel model = new SudokuModel();
    private final RemoveNumbers removeNumbers = new RemoveNumbers();
    private final SudokuController controller = new SudokuController(model, removeNumbers);

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public BoardForm() {
        JButton restartButton = new JButton("Recomeçar");
        JButton finishButton = new JButton("Finalizar");
        JToggleButton draftToggleButton = new JToggleButton("Rascunho");

        restartButton.addActionListener(e -> controller.restartGame((DefaultTableModel) sudokuTable.getModel()));
        finishButton.addActionListener(e -> controller.checkGame(sudokuTable, getMainPanel()));
        draftToggleButton.addActionListener(e -> draftMode = draftToggleButton.isSelected());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        buttonPanel.add(finishButton);
        buttonPanel.add(draftToggleButton);

        DefaultTableModel tableModel = new DefaultTableModel(9, 9) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return !model.isLocked(row, col);
            }
        };

        class SudokuCellRenderer extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("SansSerif", Font.BOLD, 18));
                setBackground(Color.WHITE); // fundo padrão

                // Célula fixa (pré-preenchida)
                if (model.isLocked(row, col)) {
                    setForeground(Color.BLACK);
                    setBackground(new Color(230, 230, 230)); // fundo cinza claro
                } else {
                    setForeground(Color.BLUE);
                }

                // Se tiver rascunho, sobrepõe o valor real
                String draft = model.getDraft(row, col);
                if (draft != null) {
                    setForeground(Color.GRAY);
                    setFont(new Font("SansSerif", Font.ITALIC, 14));
                    setText(draft);
                }

                return c;
            }
        }



        sudokuTable = new JTable(tableModel);

        SudokuCellRenderer cellRenderer = new SudokuCellRenderer();
        for (int i = 0; i < sudokuTable.getColumnCount(); i++) {
            sudokuTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        sudokuTable.setRowHeight(50);

        sudokuTable.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char ch = e.getKeyChar();
                if (!Character.isDigit(ch) || ch == '0') {
                    e.consume();
                    return;
                }

                int row = sudokuTable.getSelectedRow();
                int col = sudokuTable.getSelectedColumn();
                if (row < 0 || col < 0 || model.isLocked(row, col)) {
                    e.consume();
                    return;
                }

                if (draftMode) {
                    model.setDraft(row, col, String.valueOf(ch));
                    sudokuTable.repaint();
                } else {
                    model.setDraft(row, col, null);
                    tableModel.setValueAt(String.valueOf(ch), row, col);
                }
                int nextCol = col + 1;
                int nextRow = row;

                while (nextRow < 9) {
                    while (nextCol < 9) {
                        if (!model.isLocked(nextRow, nextCol)) {
                            sudokuTable.changeSelection(nextRow, nextCol, false, false);
                            return;
                        }
                        nextCol++;
                    }
                    nextRow++;
                    nextCol = 0;
                }
                e.consume();
            }
        });

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(sudokuTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        controller.loadInitialBoard(tableModel);
    }
}
