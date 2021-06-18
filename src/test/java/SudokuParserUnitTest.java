import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class SudokuParserUnitTest {

    private static Stream<Arguments> testSudoku_usingListener_parameters() {
        return Stream.of(
                Arguments.of("src/test/resources/sudoku/sudokuA-output.txt",
                             new int[][]{
                                     {9, 5, 7, 4, 3, 6, 8, 1, 2},
                                     {4, 3, 1, 2, 8, 5, 7, 9, 6},
                                     {8, 6, 2, 1, 9, 7, 4, 3, 5},
                                     {7, 9, 6, 8, 4, 3, 5, 2, 1},
                                     {5, 4, 3, 6, 1, 2, 9, 7, 8},
                                     {1, 2, 8, 5, 7, 9, 6, 4, 3},
                                     {2, 7, 4, 3, 5, 8, 1, 6, 9},
                                     {3, 8, 9, 7, 6, 1, 2, 5, 4},
                                     {6, 1, 5, 9, 2, 4, 3, 8, 7}
                             }),
                Arguments.of("src/test/resources/sudoku/sudokuB-output.txt",
                             new int[][]{
                                     {6, 9, 5, 3, 4, 1, 8, 7, 2},
                                     {4, 3, 7, 2, 8, 5, 9, 1, 6},
                                     {1, 8, 2, 9, 6, 7, 5, 3, 4},
                                     {8, 7, 6, 9, 4, 3, 5, 2, 1},
                                     {5, 4, 9, 6, 1, 2, 3, 7, 8},
                                     {1, 2, 3, 5, 8, 7, 6, 9, 4},
                                     {4, 7, 1, 3, 9, 2, 8, 5, 6},
                                     {3, 2, 8, 5, 6, 1, 9, 7, 4},
                                     {6, 9, 5, 4, 8, 7, 1, 2, 3}
                             })
        );
    }

    @ParameterizedTest
    @MethodSource("testSudoku_usingListener_parameters")
    void testSudoku_usingListener(String path, int[][] expectedOutput) throws IOException {
        var lexer = new Z3Lexer(CharStreams.fromString(Files.readString(Path.of(path))));
        var tokens = new CommonTokenStream(lexer);
        var parser = new Z3Parser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);

        ParseTree tree = parser.result();
        var listener = new AntlrSudokuListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        Assertions.assertArrayEquals(expectedOutput, listener.getSudokuGrid());
    }
}
