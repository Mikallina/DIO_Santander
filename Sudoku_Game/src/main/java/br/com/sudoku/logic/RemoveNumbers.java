package br.com.sudoku.logic;

import br.com.sudoku.util.RandomUtils;

import java.util.Random;

public class RemoveNumbers {

    public void removeNumbers(int[][] board, int hints) {
        int cellsToRemove = 81 - hints;

        while (cellsToRemove > 0) {
            int r = RandomUtils.nextInt(9);
            int c = RandomUtils.nextInt(9);

            if (board[r][c] != 0) {
                board[r][c] = 0;
                cellsToRemove--;
            }
        }
    }
}
