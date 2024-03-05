import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class MyUtils {
    public void writeCode(String name, String code, String grName) {
        Path DIR_PATH = Paths.get("").resolve("src/lab4gen/" + grName);

        if (!Files.exists(DIR_PATH)) {
            try {
                Files.createDirectories(DIR_PATH);
            }
            catch (IOException e) {
                System.out.println("Can't create dir");
                return;
            }
        }

        Path path = DIR_PATH.resolve(name + ".java");

        try (BufferedWriter wr = new BufferedWriter(new FileWriter(path.toFile()))){
            wr.write(code);
        }
        catch (IOException e) {
            System.out.println("Can't open file");
        }
    }
}
