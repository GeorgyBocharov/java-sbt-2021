package ru.sbt.reflection.parser.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KeyValue<K, V> {

    private final K key;
    private final V value;

    private KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static<K,V> KeyValue<K,V> of(K key, V value) {
        return new KeyValue<>(key, value);
    }

}
