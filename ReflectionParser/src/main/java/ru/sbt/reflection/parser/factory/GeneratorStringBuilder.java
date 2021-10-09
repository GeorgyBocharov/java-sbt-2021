package ru.sbt.reflection.parser.factory;

import org.apache.commons.lang3.ClassUtils;
import ru.sbt.reflection.parser.utils.KeyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Generates java code of JsonParser
 *
 * my parser can't parse Collections, Arrays and Maps because I don't want to write same shit for them
 *
 */
final class GeneratorStringBuilder {


    public static final String CLASS_NAME_PREFIX = "JsonGeneratorFor";
    public static final String PACKAGE = "ru.sbt.reflection.parser.generator";
    public static final String PACKAGE_STRING = "package " + PACKAGE + ";\n";
    public static final String PUBLIC_CLASS_JSON_GENERATOR = "public class " + CLASS_NAME_PREFIX;
    public static final String IMPLEMENTS_JSON_GENERATOR = " implements JsonGenerator<";
    public static final String TO_JSON_START = "> {\n\tpublic String toJson(";


    private GeneratorStringBuilder() {
    }

    public static <T> KeyValue<String, String> createGeneratorString(Class<T> clazz) {
        StringBuilder generatorClassBuilder = new StringBuilder();
        String simpleName = clazz.getSimpleName();
        String targetClassName = PACKAGE + '.' + CLASS_NAME_PREFIX + simpleName;
        String objectName = "item";
        String builderName = "jsonBuilder";
        generatorClassBuilder
                .append(PACKAGE_STRING)
                .append(PUBLIC_CLASS_JSON_GENERATOR)
                .append(simpleName)
                .append(IMPLEMENTS_JSON_GENERATOR)
                .append(clazz.getCanonicalName())
                .append(TO_JSON_START)
                .append(clazz.getCanonicalName())
                .append(' ')
                .append(objectName)
                .append(") {\n\t\t")
                .append("StringBuilder ")
                .append(builderName)
                .append(" = new StringBuilder();\n\t\t")
                .append(appendFunctionBody(clazz, objectName, builderName))
                .append("return ")
                .append(builderName)
                .append(".toString();\n\t}\n}");

        return KeyValue.of(targetClassName, generatorClassBuilder.toString());
    }

    private static StringBuilder appendFunctionBody(Class<?> clazz, String objectName, String builderName) {
        StringBuilder toJsonBody = new StringBuilder();
        toJsonBody
                .append(builderName)
                .append(".append(\"{\\n\\t\")\n\t\t\t")
                .append(
                        Arrays.stream(clazz.getDeclaredFields())
                                .map(field -> writeField(clazz, field, objectName, builderName))
                                .collect(Collectors.joining(".append(\",\\n\\t\")\n\t\t\t"))
                )
                .append(builderName)
                .append(".append(\"\\n}\");\n\t\t");

        return toJsonBody;
    }

    private static String writeField(Class<?> clazz, Field field, String objectName, String builderName) {
        StringBuilder fieldBuilder = new StringBuilder();
        field.setAccessible(true);
        fieldBuilder
                .append(".append(\"\\\"")
                .append(field.getName())
                .append("\\\": \");\n\t\t");
        Class<?> type = field.getType();
        System.out.println("Writing field " + field + " with typeName " + type.getTypeName());
        Method getMethod;
        try {
            getMethod = clazz.getMethod(generateGetMethod(field.getName(), type.getTypeName()));
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return "";
        }
        String methodName = objectName + '.' + getMethod.getName() + "()";

        if (ClassUtils.isPrimitiveOrWrapper(type) || type.getTypeName().equals(String.class.getTypeName())) {
            fieldBuilder
                    .append(builderName)
                    .append(".append(")
                    .append(getSimpleValue(type.getTypeName(), methodName))
                    .append(")\n\t\t\t");
        } else {
            String complexParameterClass = type.getCanonicalName();
            String complexVarName = type.getSimpleName().toLowerCase();
            fieldBuilder
                    .append(complexParameterClass)
                    .append(' ')
                    .append(complexVarName)
                    .append(" = ")
                    .append(methodName)
                    .append(";\n\t\t")
                    .append("if (")
                    .append(complexVarName)
                    .append(" == null) {\n\t\t\t")
                    .append(builderName)
                    .append(".append(\"null\");\n\t\t")
                    .append("} else {\n\t\t\t")
                    .append(builderName)
                    .append(".append(")
                    .append(GeneratorFactory.class.getName())
                    .append(".createJsonGenerator(")
                    .append(complexParameterClass)
                    .append(".class).toJson(")
                    .append(complexVarName)
                    .append("));\n\t\t}\n\t\t");
        }

        return fieldBuilder.toString();
    }

    private static String getSimpleValue(String typeName, String methodName) {
        StringBuilder fieldBuilder = new StringBuilder();
        if (typeName.equals(String.class.getTypeName())) {
            fieldBuilder
                    .append(methodName)
                    .append(" != null ? \"\\\"\" + ")
                    .append(methodName)
                    .append(" + \"\\\"\" : null");
        } else {
            fieldBuilder
                    .append(methodName);
        }
        return fieldBuilder.toString();
    }


    private static String generateGetMethod(String fieldName, String typeName) {
        String prefix = "get";
        String postfix = fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
        if (boolean.class.getTypeName().equals(typeName) || Boolean.class.getTypeName().equals(typeName)) {
            if (fieldName.startsWith("is")) {
                prefix = "";
                postfix = fieldName;
            } else {
                prefix = "is";
            }
        }
        return prefix + postfix;
    }

}
