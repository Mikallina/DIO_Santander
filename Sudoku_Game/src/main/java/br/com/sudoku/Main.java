package br.com.sudoku;

import br.com.sudoku.design.BoardForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sudoku Board");
            BoardForm boardForm = new BoardForm();
            frame.setContentPane(boardForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // centraliza na tela
            frame.setVisible(true);
        });
    }
}