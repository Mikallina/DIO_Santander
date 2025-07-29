import br.com.sudoku.logic.SudokuGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SudokuGeneratorTest {

    @Test
    void testGeneratedBoardIsValid() {
        SudokuGenerator generator = new SudokuGenerator();
        int[][] board = generator.generate();

        assertTrue(isValidSudoku(board), "O tabuleiro gerado não é válido.");
    }

    private boolean isValidSudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] rowCheck = new boolean[9];
            boolean[] colCheck = new boolean[9];
            boolean[] boxCheck = new boolean[9];

            for (int j = 0; j < 9; j++) {
                int rowVal = board[i][j] - 1;
                int colVal = board[j][i] - 1;
                int boxVal = board[3 * (i / 3) + j / 3][3 * (i % 3) + j % 3] - 1;

                if (rowVal < 0 || rowVal >= 9 || rowCheck[rowVal]) return false;
                if (colVal < 0 || colVal >= 9 || colCheck[colVal]) return false;
                if (boxVal < 0 || boxVal >= 9 || boxCheck[boxVal]) return false;

                rowCheck[rowVal] = colCheck[colVal] = boxCheck[boxVal] = true;
            }
        }
        return true;
    }
}
