package ru.sbt.decoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassFileBytesDecoder implements EncryptedBytesDecoder {

    private static final byte[] DEFAULT_MAGIC_NUMBER = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
    private static final int ENCRYPTION_SHIFT = 1;


    @Override
    public byte[] decode(int bytesToSkip, byte[]  bytes) {
        if (bytes.length < bytesToSkip + DEFAULT_MAGIC_NUMBER.length) {
            throw new RuntimeException("Failed to decode byteArray, because it's too short");
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] -= ENCRYPTION_SHIFT;
        }
        int newSize = bytes.length - bytesToSkip;
        byte[] result = new byte[newSize];
        System.arraycopy(DEFAULT_MAGIC_NUMBER, 0, result, 0, DEFAULT_MAGIC_NUMBER.length);
        System.arraycopy(bytes, DEFAULT_MAGIC_NUMBER.length + bytesToSkip,
                result, DEFAULT_MAGIC_NUMBER.length, newSize - DEFAULT_MAGIC_NUMBER.length);
        return result;
    }
}
