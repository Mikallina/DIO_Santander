


import br.com.sudoku.logic.PuzzleBuilder;
import br.com.sudoku.logic.SudokuGenerator;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PuzzleBuilderTest {

    @Test
    public void testCorrectHintCount() {
        SudokuGenerator generator = new SudokuGenerator();
        PuzzleBuilder builder = new PuzzleBuilder();

        int[][] solution = generator.generate();
        int hints = 30;
        int[][] puzzle = builder.createPuzzle(solution, hints);

        int filled = countNonZeroCells(puzzle);

        assertEquals(hints, filled, "Número de dicas não está correto.");
    }

    private int countNonZeroCells(int[][] board) {
        int count = 0;
        for (int[] row : board) {
            for (int val : row) {
                if (val != 0) count++;
            }
        }
        return count;
    }
}
