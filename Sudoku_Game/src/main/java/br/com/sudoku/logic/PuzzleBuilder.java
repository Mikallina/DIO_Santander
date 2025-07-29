package br.com.sudoku.logic;

import br.com.sudoku.util.RandomUtils;

public class PuzzleBuilder {

    public int[][] createPuzzle(int[][] fullBoard, int hints) {
        int[][] puzzle = deepCopy(fullBoard);
        removeNumbers(puzzle, hints);
        return puzzle;
    }

    private void removeNumbers(int[][] board, int hints) {
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

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}
