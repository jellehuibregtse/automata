public class AntlrSudokuListener extends SudokuBaseListener {

    // The size of the sudoku.
    final int SUDOKU_SIZE = 9;
    private final int[][] sudokuGrid = new int[SUDOKU_SIZE][SUDOKU_SIZE];
    
    public void print() {
        System.out.println("+-----------------+");
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            System.out.print("|");
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (sudokuGrid[i][j] == 0) {
                    System.out.print((j + 1) % 3 == 0 ? " " : "  ");
                } else {
                    System.out.print((j + 1) % 3 == 0 ? sudokuGrid[i][j] : sudokuGrid[i][j] + " ");
                }
                if ((j + 1) % 3 == 0 && (j + 1) != SUDOKU_SIZE) {
                    System.out.print("|");
                }
            }
            if ((i + 1) % 3 == 0 && (i + 1) != SUDOKU_SIZE) {
                System.out.print("|\n------------------");
            }
            System.out.print((i + 1) % 3 == 0 && (i + 1) != SUDOKU_SIZE ? "-\n" : "|\n");
        }
        System.out.println("+-----------------+");
    }

    @Override
    public void exitValue(SudokuParser.ValueContext ctx) {
        int x = Integer.parseInt(ctx.field().NUMBER(0).getText()) - 1;
        int y = Integer.parseInt(ctx.field().NUMBER(1).getText()) - 1;
        int value = Integer.parseInt(ctx.NUMBER().getText());

        sudokuGrid[x][y] = value;
    }
}
