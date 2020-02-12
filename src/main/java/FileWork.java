import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


class FileWork {
    private static String api_file = System.getProperty("user.dir") + "\\src\\main\\resources\\api_key.txt";

    static String readApiKey() throws IOException {
        return Files.readAllLines(Paths.get(api_file)).get(0);
    }
}
