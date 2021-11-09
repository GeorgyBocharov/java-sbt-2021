package ru.sbt.reflection.parser.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class JsonParserClassCodeContainer {

    private final String className;
    private final String classCode;

}
