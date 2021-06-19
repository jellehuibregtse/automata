import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        var run = true;

        while (run) {
            String input = getUserInput();

            if (input.equals("end")) {
                run = false;
            }

            try {
                z3(input);
            } catch (ParseCancellationException e) {
                try {
                    listener(input);
                } catch (ParseCancellationException e2) {
                    try {
                        visitor(input);
                    } catch (ParseCancellationException e3) {
                        System.err.println("Invalid input provided");
                        System.err.println("Z3: " + e.getMessage());
                        System.err.println("Listener: " + e2.getMessage());
                        System.err.println("Visitor: " + e3.getMessage());
                    }
                }
            }
        }
    }

    private static void z3(String input) {
        var lexer = new Z3Lexer(CharStreams.fromString(input));
        var tokens = new CommonTokenStream(lexer);
        var parser = new Z3Parser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);

        ParseTree tree = parser.result();
        var listener = new AntlrZ3Listener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        listener.print();

        new GenerateOutput(listener.getOut());
    }

    private static void visitor(String input) {
        var lexer = new ArithmeticLexer(CharStreams.fromString(input));
        var tokens = new CommonTokenStream(lexer);
        var parser = new ArithmeticParser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);

        ParseTree tree = parser.program();
        var visitor = new AntlrArithmeticVisitor();
        visitor.visit(tree);

        new GenerateOutput(visitor.getOut());
    }

    private static void listener(String input) {
        var lexer = new ExpressionsLexer(CharStreams.fromString(input));
        var tokens = new CommonTokenStream(lexer);
        var parser = new ExpressionsParser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);

        ParseTree tree = parser.expression();
        var listener = new AntlrExpressionsListener(true);
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        new GenerateOutput(listener.getOut());
    }

    private static String getUserInput() throws IOException {
        System.out.println("Please enter the path to the input file below.");
        return getFile(new Scanner(System.in).nextLine());
    }

    private static String getFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
