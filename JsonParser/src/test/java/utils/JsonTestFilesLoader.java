package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class JsonTestFilesLoader {

    private JsonTestFilesLoader() {}

    public static String INDIVIDUAL_CLIENT_FILE = "individual_client.json";
    public static String HOLDING_CLIENT_FILE = "holding_client.json";
    public static String INDIVIDUAL_CLIENT_LIST_FILE = "individual_client_list.json";

    public static String loadJsonFile(String fileName) throws IOException {
        Path resourceDirectory = Paths.get("src","test","resources", "json", fileName);
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        Path jsonFile = Path.of(absolutePath);
        return Files.readString(jsonFile);
    }
}
