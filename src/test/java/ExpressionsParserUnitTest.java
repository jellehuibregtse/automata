import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ExpressionsParserUnitTest {

    @ParameterizedTest
    @MethodSource("testExpressions_usingListener_parameters")
    void testExpressions_usingListener(String expression, int expectedOutput, boolean printSteps) {
        ExpressionsLexer lexer = new ExpressionsLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionsParser parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        AntlrExpressionsListener listener = new AntlrExpressionsListener(printSteps);
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        Assertions.assertEquals(expectedOutput, listener.getResult());
    }

    private static Stream<Arguments> testExpressions_usingListener_parameters() {
        return Stream.of(
                Arguments.of("9+10*23", 239, true),
                Arguments.of("return 9+10*23", 239, false),
                Arguments.of("var test1 = 9+10*23\r\nprint(test1)\r\nvar test2 = 250*2\r\nprint(test2)\r\nreturn test1", 239, false)
        );
    }
}
