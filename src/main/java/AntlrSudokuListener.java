import lombok.Getter;

public class AntlrSudokuListener extends SudokuBaseListener {

    // The size of the sudoku.
    private static final int SUDOKU_SIZE = 9;
    private final int[][] sudokuGrid = new int[SUDOKU_SIZE][SUDOKU_SIZE];
    @Getter
    private String out = "";

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
        if (ctx.expression().ite() == null) {
            // Sudoku A.
            var x = Integer.parseInt(ctx.field().NUMBER(0).getText()) - 1;
            var y = Integer.parseInt(ctx.field().NUMBER(1).getText()) - 1;
            var value = Integer.parseInt(ctx.expression().getText());

            sudokuGrid[x][y] = value;

        } else {
            // Sudoku B.
            var x = Integer.parseInt(ctx.expression().ite().expression(0).and().expression(0).equals(0).expression(1).getText()) - 1;
            var y = Integer.parseInt(ctx.expression().ite().expression(0).and().expression(0).equals(1).expression(1).getText()) - 1;

            var value = Integer.parseInt(ctx.expression().ite().expression(1).getText());

            sudokuGrid[x][y] = value;

            handleRecursion(ctx.expression().ite());
        }
    }

    private void handleRecursion(SudokuParser.IteContext ite) {
        if (ite == null) {
            return;
        }

        var x = Integer.parseInt(ite.expression(0).and().expression(0).equals(0).expression(1).getText()) - 1;
        var y = Integer.parseInt(ite.expression(0).and().expression(0).equals(1).expression(1).getText()) - 1;

        var value = Integer.parseInt(ite.expression(1).getText());

        sudokuGrid[x][y] = value;

        handleRecursion(ite.expression(2).ite());
    }
}
