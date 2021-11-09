package ru.sbt.reflection.parser.reflection;

import org.apache.commons.lang3.ClassUtils;

import java.util.Collection;

public enum ClassType {
    PRIMITIVE_OR_WRAPPER,
    STRING,
    COLLECTION,
    ARRAY,
    OTHER;

    public static ClassType getTypeByClass(Class<?> clazz) {
        if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
            return PRIMITIVE_OR_WRAPPER;
        } else if (clazz.getTypeName().equals(String.class.getTypeName())) {
            return STRING;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return COLLECTION;
        } else if (clazz.isArray()) {
            return ARRAY;
        } else {
            return OTHER;
        }
    }
}
