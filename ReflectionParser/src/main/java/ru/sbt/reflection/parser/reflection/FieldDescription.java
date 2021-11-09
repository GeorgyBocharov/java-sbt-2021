package ru.sbt.reflection.parser.reflection;

import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;


@Getter
@ToString
public class FieldDescription {
    private final Field field;
    private final String fieldName;
    private final String getterName;
    private final ClassRepresentation fieldClassRepresentation;

    public FieldDescription(Field field, String getterName) {
        this.field = field;
        this.getterName = getterName;
        this.fieldName = field.getName();
        fieldClassRepresentation = new ClassRepresentation(field.getType());
    }
}
