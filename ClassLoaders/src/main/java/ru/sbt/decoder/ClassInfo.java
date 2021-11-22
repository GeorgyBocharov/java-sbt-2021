package ru.sbt.decoder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ClassInfo {
    private final String className;
    private final byte[] bytes;
}
