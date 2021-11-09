package ru.sbt.reflection.parser.reflection;

import lombok.Getter;
import lombok.ToString;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.sbt.reflection.parser.reflection.ClassType.getTypeByClass;

@Getter
@ToString
public class ClassRepresentation {

    private final Class<?> objectClass;
    private final ClassType type;

    public ClassRepresentation(Class<?> objectClass) {
        this.objectClass = objectClass;
        type = getTypeByClass(objectClass);
    }

    public List<FieldDescription> retrieveFieldDescriptions() {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !field.isSynthetic())
                .map(this::getFieldWithGetter)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<FieldDescription> getFieldWithGetter(Field field) {
        field.setAccessible(true);
        String getterName = null;
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), objectClass);
            getterName = pd.getReadMethod().getName();
        } catch (IntrospectionException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Failed to find getter for " + field);
        }
        return getterName != null ? Optional.of(new FieldDescription(field, getterName)) : Optional.empty();
    }

}
