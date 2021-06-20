import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateOutput {
    public GenerateOutput(String text) {

        var path = "./target/generated-sources/output";
        var directory = new File(path);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "/output.txt";
        if (text.contains("automaton")) {
            fileName = "/output.dot";
        }

        try (var writer = new FileWriter(path + fileName)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (text.contains("automaton")) {
            try {
                toPNG(path, fileName);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void toPNG(String path, String file) throws IOException, InterruptedException {
        var runtime = Runtime.getRuntime();
        var process = runtime.exec("dot -Tpng -o " + (path + "/output.png") + " " + path + file);
        process.waitFor();
    }
}
