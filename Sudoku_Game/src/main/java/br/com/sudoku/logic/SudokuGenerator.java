package br.com.sudoku.logic;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private final int[][] board = new int[SIZE][SIZE];

    public int[][] generate() {
        fillBoard(0, 0);
        return deepCopy(board);
    }

    private boolean fillBoard(int row, int col) {
        if (col == SIZE) {
            col = 0;
            row++;
            if (row == SIZE) return true;
        }

        List<Integer> numbers = IntStream.rangeClosed(1, SIZE).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (canPlace(row, col, num)) {
                board[row][col] = num;
                if (fillBoard(row, col + 1)) return true;
            }
        }

        board[row][col] = 0;
        return false;
    }

    private boolean canPlace(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;

        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (board[r][c] == num) return false;
            }
        }

        return true;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }
}
