package ru.sbt;

import ru.sbt.classloader.CustomClassloader;
import ru.sbt.decoder.ClassFileBytesDecoder;
import ru.sbt.decoder.EncryptedBytesDecoder;
import ru.sbt.web.loader.WebDataLoader;
import ru.sbt.web.loader.WebDataLoaderImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int ARGS_NUMBER = 4;
    public static final int FIRST_CLASS_URL_INDEX = 1;
    public static final int SECOND_CLASS_URL_INDEX = 3;
    public static final int FIRST_CLASS_NAME_INDEX = 0;
    public static final int SECOND_CLASS_NAME_INDEX = 2;


    public static void main(String[] args) {
        Map<String, URL> nameToUrlMap = createNameToUrlMap(args);
        if (nameToUrlMap.isEmpty()) {
            return;
        }

        WebDataLoader webDataLoader = new WebDataLoaderImpl();
        EncryptedBytesDecoder encryptedBytesDecoder = new ClassFileBytesDecoder();

        URLClassLoader webClassLoader = new CustomClassloader(webDataLoader, encryptedBytesDecoder, nameToUrlMap);

        try {
            Class<?> secretClass = webClassLoader.loadClass(args[FIRST_CLASS_NAME_INDEX]);
            Class<?> veryStrangeSecretClass = webClassLoader.loadClass(args[SECOND_CLASS_NAME_INDEX]);

            List.of(
                    (Runnable) secretClass.getConstructor().newInstance(),
                    (Runnable) veryStrangeSecretClass.getConstructor().newInstance()
            ).forEach(Runnable::run);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static Map<String, URL> createNameToUrlMap(String[] args) {
        if (args.length != ARGS_NUMBER) {
            throw new IllegalArgumentException(String.format("Expected %d args but got %d", ARGS_NUMBER, args.length));
        }
        URL secretURL;
        URL veryStrangeSecretURL;
        try {
            secretURL = new URL(args[FIRST_CLASS_URL_INDEX]);
            veryStrangeSecretURL = new URL(args[SECOND_CLASS_URL_INDEX]);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return Collections.emptyMap();
        }
        return Map.of(args[FIRST_CLASS_NAME_INDEX], secretURL, args[SECOND_CLASS_NAME_INDEX], veryStrangeSecretURL);
    }

}
