package ru.sbt.decoder;

import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RequiredArgsConstructor
public class ClassInfoBytesDecoderImpl implements ClassInfoBytesDecoder {

    private static final byte[] DEFAULT_MAGIC_NUMBER = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
    private static final int ENCRYPTION_SHIFT = 1;

    @Override
    public ClassInfo decodeBytes(byte[]  bytes) {
        if (bytes.length < DEFAULT_MAGIC_NUMBER.length) {
            throw new RuntimeException("Failed to decode byteArray, because it's too short");
        }
        decryptBytes(bytes);
        int classNameLength = getClassNameLength(bytes);
        String className = decryptClassName(bytes, classNameLength);
        byte[] classBytes = decodeClassBytes(classNameLength, bytes);
        return new ClassInfo(className, classBytes);
    }

    private void decryptBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] -= ENCRYPTION_SHIFT;
        }
    }

    private int getClassNameLength(byte[] decryptedBytes) {
        boolean magicNumberFound;
        for (int i = 0; i < decryptedBytes.length; i++) {
            byte decryptedByte = decryptedBytes[i];
            if (decryptedByte == DEFAULT_MAGIC_NUMBER[0]) {
                magicNumberFound = true;
                for (int j = 1; j < DEFAULT_MAGIC_NUMBER.length; j++) {
                    magicNumberFound = magicNumberFound && (decryptedBytes[i + j] == DEFAULT_MAGIC_NUMBER[j]);
                }
                if (magicNumberFound) {
                    return i;
                }
            }
        }
        throw new RuntimeException("Decrypted array doesn't contain magic number");
    }

    private String decryptClassName(byte[] decryptedBytes, int classNameLength) {
        return new String(Arrays.copyOf(decryptedBytes, classNameLength), StandardCharsets.UTF_8);
    }

    private byte[] decodeClassBytes(int classNameLength, byte[] decryptedBytes) {
        int newSize = decryptedBytes.length - classNameLength;
        byte[] result = new byte[newSize];
        System.arraycopy(DEFAULT_MAGIC_NUMBER, 0, result, 0, DEFAULT_MAGIC_NUMBER.length);
        System.arraycopy(decryptedBytes, DEFAULT_MAGIC_NUMBER.length + classNameLength,
                result, DEFAULT_MAGIC_NUMBER.length, newSize - DEFAULT_MAGIC_NUMBER.length);
        return result;
    }
}
