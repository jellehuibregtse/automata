import org.antlr.v4.runtime.CharStreams;
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
        // Get input from user.
        System.out.println("Please enter the path to the input file below.");
        var input = CharStreams.fromString(getFile(new Scanner(System.in).nextLine()));
        var lexer = new ExpressionsLexer(input);
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

    private static String getFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
