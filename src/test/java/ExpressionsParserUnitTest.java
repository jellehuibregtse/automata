import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionsParserUnitTest {

    @Test
    void simpleTest() {
        String expression = "9+10*23\r\n";
        ExpressionsLexer lexer = new ExpressionsLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionsParser parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        AntlrExpressionsListener listener = new AntlrExpressionsListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        Assertions.assertEquals(239, listener.getResult());
    }
}
