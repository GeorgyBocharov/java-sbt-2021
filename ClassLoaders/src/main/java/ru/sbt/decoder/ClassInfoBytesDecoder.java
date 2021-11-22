package ru.sbt.decoder;

public interface ClassInfoBytesDecoder {
    ClassInfo decodeBytes(byte[]  bytes);
}
