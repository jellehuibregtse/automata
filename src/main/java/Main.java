import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Sudoku();
    }

    private static void Sudoku() throws IOException {
        // Get input from user.
        var lexer = new SudokuLexer(getUserInput());
        var tokens = new CommonTokenStream(lexer);
        var parser = new SudokuParser(tokens);

        ParseTree tree = parser.result();
        var listener = new AntlrSudokuListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        listener.print();
    }

    private static void Visitor() throws IOException {
        var lexer = new ArithmeticLexer(getUserInput());
        var tokens = new CommonTokenStream(lexer);
        var parser = new ArithmeticParser(tokens);

        ParseTree tree = parser.program();
        AntlrArithmeticVisitor visitor = new AntlrArithmeticVisitor();
        visitor.visit(tree);
    }

    private static void Listener() throws IOException {
        var lexer = new ExpressionsLexer(getUserInput());
        var tokens = new CommonTokenStream(lexer);
        var parser = new ExpressionsParser(tokens);

        ParseTree tree = parser.expression();
        var listener = new AntlrExpressionsListener(true);
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        // Generate output.txt
        var directory = new File("./output");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (var out = new PrintWriter("./output/output.txt")) {
            out.println(listener.getResult());
        }
    }

    private static CodePointCharStream getUserInput() throws IOException {
        // Get input from user.
        System.out.println("Please enter the path to the input file below.");
        return CharStreams.fromString(getFile(new Scanner(System.in).nextLine()));
    }

    private static String getFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
