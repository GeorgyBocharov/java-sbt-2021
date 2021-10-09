package ru.sbt.reflection.parser.generator;

public interface JsonGenerator<T> {
    String toJson(T item);
}
