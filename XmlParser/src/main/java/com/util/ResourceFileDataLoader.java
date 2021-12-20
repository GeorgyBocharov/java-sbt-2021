package com.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceFileDataLoader {

    private ResourceFileDataLoader() {
    }

    public static String loadFileFromMainResources(String fileName) throws IOException {
        Path resourceDirectory = Paths.get("src","main", "resources", fileName);
        return getFileAsString(resourceDirectory);
    }

    public static String loadFileFromTestResources(String fileName) throws IOException {
        Path resourceDirectory = Paths.get("src","test", "resources", fileName);
        return getFileAsString(resourceDirectory);
    }

    private static String getFileAsString(Path resourceDirectory) throws IOException {
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return Files.readString(Path.of(absolutePath));
    }
}
