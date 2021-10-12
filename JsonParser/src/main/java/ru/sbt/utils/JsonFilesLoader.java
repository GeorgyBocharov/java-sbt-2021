package ru.sbt.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class JsonFilesLoader {

    private JsonFilesLoader() {}

    public static Optional<String> loadJsonFile(String absolutePath) {
        try {
            Path file = Path.of(absolutePath);
            return Optional.of(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

}
