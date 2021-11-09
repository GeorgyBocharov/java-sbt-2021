package ru.sbt.web.loader;

import java.net.URL;

public interface WebDataLoader {
    byte[] loadData(URL url);
}
