import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var input = CharStreams.fromString(new Scanner(System.in).nextLine() + "\n");
        var lexer = new ExpressionsLexer(input);
        var tokens = new CommonTokenStream(lexer);
        var parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        var listener = new AntlrExpressionsListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);
    }
}
