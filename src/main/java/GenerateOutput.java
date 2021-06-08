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

        try {
            FileWriter myWriter = new FileWriter(path + "/output.txt");
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
