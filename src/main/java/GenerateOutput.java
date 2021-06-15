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

        try (var writer = new FileWriter(path + "/output.txt")) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
