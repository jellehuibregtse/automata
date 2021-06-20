import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MockExam {
    public static void main(String[] args) throws IOException {
        var lexer = new MockExamLexer(CharStreams.fromString(getUserInput()));
        var tokens = new CommonTokenStream(lexer);
        var parser = new MockExamParser(tokens);

        ParseTree tree = parser.program();
        var listener = new AntlrMockExamListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);
    }

    private static String getUserInput() throws IOException {
        System.out.println("Please enter the path to the input file below.");
        return getFile(new Scanner(System.in).nextLine());
    }

    private static String getFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
