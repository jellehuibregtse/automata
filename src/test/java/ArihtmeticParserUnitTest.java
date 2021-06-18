import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class ArithmeticParserUnitTest {

    private static Stream<Arguments> testArithmetic_usingVisitor_parameters() {
        return Stream.of(
                Arguments.of("src/test/resources/visitor/input1.txt", "a + b = 15"),
                Arguments.of("src/test/resources/visitor/input2.txt", "power\n" +
                                                                      "10 ** 2 = 100\n" +
                                                                      "modulus\n" +
                                                                      "10 % 2 = 0\n" +
                                                                      "10 % 3 = 1\n" +
                                                                      "multiplication\n" +
                                                                      "10 / 3 = 3\n" +
                                                                      "11 / 3 = 3\n" +
                                                                      "12 / 3 = 4"),
                Arguments.of("src/test/resources/visitor/input3.txt", "n = 0\n" +
                                                                      "n = 1\n" +
                                                                      "n = 2\n" +
                                                                      "n = 3\n" +
                                                                      "n = 4\n" +
                                                                      "n = 5\n" +
                                                                      "n = 6\n" +
                                                                      "n = 7\n" +
                                                                      "n = 8\n" +
                                                                      "n = 9\n" +
                                                                      "10\n" +
                                                                      "20\n" +
                                                                      "30\n" +
                                                                      "40\n" +
                                                                      "50\n" +
                                                                      "60\n" +
                                                                      "70\n" +
                                                                      "80\n" +
                                                                      "90\n" +
                                                                      "100"),
                Arguments.of("src/test/resources/visitor/input4.txt", "We add two numbers.\n" +
                                                                      "15\n" +
                                                                      "We add two numbers.\n" +
                                                                      "We compare two numbers\n" +
                                                                      "greater\n" +
                                                                      "We compare two numbers\n" +
                                                                      "equal\n" +
                                                                      "We add two numbers.\n" +
                                                                      "We compare two numbers\n" +
                                                                      "smaller")
        );
    }

    @ParameterizedTest
    @MethodSource("testArithmetic_usingVisitor_parameters")
    void testArithmetic_usingVisitor(String path, String expectedOutput) throws IOException {
        var lexer = new ArithmeticLexer(CharStreams.fromString(Files.readString(Path.of(path))));
        var tokens = new CommonTokenStream(lexer);
        var parser = new ArithmeticParser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);

        ParseTree tree = parser.program();
        var visitor = new AntlrArithmeticVisitor();
        visitor.visit(tree);

        Assertions.assertEquals(expectedOutput, visitor.getOut());
    }
}
