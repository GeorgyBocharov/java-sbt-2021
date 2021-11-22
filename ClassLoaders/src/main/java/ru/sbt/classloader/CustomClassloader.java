package ru.sbt.classloader;

import ru.sbt.decoder.ClassInfoBytesDecoder;
import ru.sbt.web.loader.WebDataLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomClassloader extends URLClassLoader {

    private final WebDataLoader webDataLoader;
    private final ClassInfoBytesDecoder encryptedBytesDecoder;
    private final Map<String, byte[]> nameToBytes = new HashMap<>();

    public CustomClassloader(WebDataLoader webDataLoader, ClassInfoBytesDecoder classInfoBytesDecoder, URL[] urls) {
        super(urls);
        this.webDataLoader = webDataLoader;
        this.encryptedBytesDecoder = classInfoBytesDecoder;
        fillNameToBytesMap(urls);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = nameToBytes.get(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    private void fillNameToBytesMap(URL[] urls) {
        Arrays.stream(urls)
                .map(webDataLoader::loadData)
                .map(encryptedBytesDecoder::decodeBytes)
                .forEach(info -> nameToBytes.put(info.getClassName(), info.getBytes()));
    }
}
