import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionsParserUnitTest {

    @Test
    void simpleTest() {
        String expression = "9+10*23";
        ExpressionsLexer lexer = new ExpressionsLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionsParser parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        AntlrExpressionsListener listener = new AntlrExpressionsListener(true);
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        Assertions.assertEquals(239, listener.getResult());
    }

    @Test
    void returnTest() {
        String expression = "return 9+10*23";
        ExpressionsLexer lexer = new ExpressionsLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionsParser parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        AntlrExpressionsListener listener = new AntlrExpressionsListener(false);
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        Assertions.assertEquals(239, listener.getResult());
    }

    @Test
    void varTest() {
        String expression = "var test1 = 9+10*23\r\nprint(test1)\r\nvar test2 = 250*2\r\nprint(test2)\r\nreturn test1";
        ExpressionsLexer lexer = new ExpressionsLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionsParser parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        AntlrExpressionsListener listener = new AntlrExpressionsListener(false);
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        Assertions.assertEquals(239, listener.getResult());
    }
}
