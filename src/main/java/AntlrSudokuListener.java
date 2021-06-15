import lombok.Getter;

public class AntlrSudokuListener extends SudokuBaseListener {

    // The size of the sudoku.
    private static final int SUDOKU_SIZE = 9;
    private final int[][] sudokuGrid = new int[SUDOKU_SIZE][SUDOKU_SIZE];
    @Getter private String out = "";

    public void print() {
        printLn("+-----------------+");
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            print("|");
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (sudokuGrid[i][j] == 0) {
                    print((j + 1) % 3 == 0 ? " " : "  ");
                } else {
                    print((j + 1) % 3 == 0 ? String.valueOf(sudokuGrid[i][j]).replace("\n", "") : String.valueOf(sudokuGrid[i][j]).replace("\n", "") + " ");
                }
                if ((j + 1) % 3 == 0 && (j + 1) != SUDOKU_SIZE) {
                    print("|");
                }
            }
            if ((i + 1) % 3 == 0 && (i + 1) != SUDOKU_SIZE) {
                print("|\n------------------");
            }
            print((i + 1) % 3 == 0 && (i + 1) != SUDOKU_SIZE ? "-\n" : "|\n");
        }
        printLn("+-----------------+");
    }

    private void print(String message) {
        out += message;
        System.out.print(message);
    }

    private void printLn(String message) {
        out += message + "\n";
        System.out.println(message);
    }

    @Override
    public void exitValue(SudokuParser.ValueContext ctx) {
        int x = Integer.parseInt(ctx.field().NUMBER(0).getText()) - 1;
        int y = Integer.parseInt(ctx.field().NUMBER(1).getText()) - 1;
        var value = Integer.parseInt(ctx.NUMBER().getText());

        sudokuGrid[x][y] = value;
    }
}
