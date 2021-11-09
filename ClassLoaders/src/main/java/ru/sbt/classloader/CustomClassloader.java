package ru.sbt.classloader;

import ru.sbt.decoder.EncryptedBytesDecoder;
import ru.sbt.web.loader.WebDataLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class CustomClassloader extends URLClassLoader {

    private final WebDataLoader webDataLoader;
    private final EncryptedBytesDecoder encryptedBytesDecoder;
    private final Map<String, URL> nameToUrl;

    public CustomClassloader(WebDataLoader webDataLoader, EncryptedBytesDecoder encryptedBytesDecoder, Map<String, URL> nameToUrl) {
        super(nameToUrl.values().toArray(new URL[]{}));
        this.webDataLoader = webDataLoader;
        this.encryptedBytesDecoder = encryptedBytesDecoder;
        this.nameToUrl = nameToUrl;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        URL url = nameToUrl.get(name);
        if (url == null) {
            throw new ClassNotFoundException(name);
        }
        byte[] bytes = encryptedBytesDecoder.decode(name.length(), webDataLoader.loadData(url));
        return defineClass(name, bytes, 0, bytes.length);
    }

}
