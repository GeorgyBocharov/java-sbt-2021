package ru.sbt.decoder;

public interface EncryptedBytesDecoder {
    byte[] decode(int bytesToSkip, byte[]  bytes);
}
