package br.com.sudoku.model;

public class SudokuModel {
    private int[][] solution;
    private boolean[][] lockedCells = new boolean[9][9];
    private String[][] draftValues = new String[9][9];
    public SudokuModel() {
        solution = new int[9][9];
    }
    public void setSolution(int[][] solution) {
        this.solution = solution;
    }
    public int getSolutionValue(int row, int col) {
        return solution[row][col];
    }
    public boolean isLocked(int row, int col) {
        return lockedCells[row][col];
    }
    public void setLocked(int row, int col, boolean locked) {
        lockedCells[row][col] = locked;
    }
    public void setDraft(int row, int col, String value) {
        draftValues[row][col] = value;
    }
    public String getDraft(int row, int col) {
        return draftValues[row][col];
    }

    public void reset() {
        solution = new int[9][9];
        lockedCells = new boolean[9][9];
        draftValues = new String[9][9];
    }
}

