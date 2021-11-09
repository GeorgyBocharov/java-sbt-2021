package ru.sbt.reflection.parser.factory;

import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;

import ru.sbt.reflection.parser.reflection.ClassType;
import ru.sbt.reflection.parser.reflection.FieldDescription;
import ru.sbt.reflection.parser.reflection.ClassRepresentation;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static ru.sbt.reflection.parser.reflection.ClassType.ARRAY;
import static ru.sbt.reflection.parser.reflection.ClassType.COLLECTION;
import static ru.sbt.reflection.parser.reflection.ClassType.PRIMITIVE_OR_WRAPPER;
import static ru.sbt.reflection.parser.reflection.ClassType.STRING;

final class JsonParserCodeGenerator {
    private static final String CLASS_NAME_PREFIX = "JsonGeneratorFor";
    private static final String PACKAGE = "ru.sbt.reflection.parser.generator";

    private static final Random random = new Random();
    public static final String CREATE_JSON_GENERATOR_FOR_COLLECTION = ".createJsonGeneratorForCollection(";
    public static final String CREATE_JSON_GENERATOR = ".createJsonGenerator(";

    private JsonParserCodeGenerator() {
    }

    public static <T> JsonParserClassCodeContainer createCodeContainerForCollection(Collection<T> collection) {
        T t = collection.iterator().next();
        Class<?> genericClass = t.getClass();
        String namePostfix = Collection.class.getSimpleName() + genericClass.getSimpleName() + getId();
        String targetClassName = PACKAGE + '.' + CLASS_NAME_PREFIX + namePostfix;
        String genericVarName = genericClass.getSimpleName().toLowerCase() + getId();

        String genericClassCanonicalName = genericClass.getCanonicalName();
        String str =
                getClassBeginning("import java.util.Collection;\n", namePostfix, "Collection", "collection") +
                        "{{builderName}}.append(\"[\\n\");\n\t\t" +
                        "for (Object object: collection) {\n\t\t\t" +
                        String.format("%s %s = (%s) object;\n\t\t", genericClassCanonicalName, genericVarName, genericClassCanonicalName) +
                        appendFunctionBody(new ClassRepresentation(genericClass), genericVarName) +
                        "{{builderName}}.append(',');\n\t\t}\n\t\t" +
                        "{{builderName}}.replace({{builderName}}.length() - 1, {{builderName}}.length(), \"\").append(\"\\n]\");\n\t\t" +
                        "return {{builderName}}.toString();\n\t}\n}";

        str = StringSubstitutor.replace(str, Map.of("builderName", "jsonBuilder"), "{{", "}}");
        return new JsonParserClassCodeContainer(targetClassName, str);
    }

    public static <T> JsonParserClassCodeContainer createCodeContainer(T object) {
        Class<?> clazz = object.getClass();
        String namePostfix = clazz.getSimpleName().replaceAll("[^A-Za-z]", "") + getId();
        String clazzCanonicalName = clazz.getCanonicalName();
        String targetClassName = PACKAGE + '.' + CLASS_NAME_PREFIX + namePostfix;
        String objectName = "item";

        String str = getClassBeginning("", namePostfix, clazzCanonicalName, objectName) +
                appendFunctionBody(new ClassRepresentation(clazz), objectName) +
                "return {{builderName}}.toString();\n\t}\n}";

        str = StringSubstitutor.replace(str, Map.of("builderName", "jsonBuilder"), "{{", "}}");

        return new JsonParserClassCodeContainer(targetClassName, str);
    }

    private static String appendFunctionBody(ClassRepresentation classRepresentation, String functionInputName) {
        String functionBody;
        ClassType classType = classRepresentation.getType();

        if (classType == ARRAY) {
            Class<?> arrayElemClass = classRepresentation.getObjectClass().getComponentType();
            String elemName = arrayElemClass.getSimpleName().toLowerCase() + getId();
            functionBody = "{{builderName}}.append(\"[\\n\\t\");\n\t\t" +
                    String.format("for (%s %s: %s) {\n\t\t\t", arrayElemClass.getName(), elemName, functionInputName) +
                    appendValue(new ClassRepresentation(arrayElemClass), elemName) +
                    "{{builderName}}.append(',');\n}\n\t\t" +
                    "{{builderName}}.replace({{builderName}}.length()-1, {{builderName}}.length(), \"\");\n\t\t" +
                    "{{builderName}}.append(\"]\\n\\t\");\n\t";
        } else {
            functionBody = writeClass(classRepresentation, functionInputName);
        }

        return functionBody;
    }

    private static String writeClass(ClassRepresentation classRepresentation, String objectName) {
        ClassType classType = classRepresentation.getType();

        if (classType == PRIMITIVE_OR_WRAPPER || classType == STRING) {
            return "{{builderName}}.append(" + getSimpleValue(classType, objectName) + ");\n\t\t";
        }

        String fieldsDescription = classRepresentation.retrieveFieldDescriptions().stream()
                .map(fieldDescription -> writeField(fieldDescription, objectName))
                .collect(Collectors.joining("{{builderName}}.append(\",\\n\\t\");\n\t\t"));

        return "{{builderName}}.append(\"{\\n\\t\");\n\t\t" + fieldsDescription + "{{builderName}}.append(\"\\n}\");\n\t\t";
    }

    private static String writeField(FieldDescription fieldDescription, String objectName) {
        StringBuilder fieldBuilder = new StringBuilder();

        String getterName = fieldDescription.getGetterName();
        fieldBuilder
                .append("{{builderName}}.append(\"\\\"")
                .append(fieldDescription.getFieldName())
                .append("\\\": \");\n\t\t");

        String methodName = objectName + '.' + getterName + "()";

        return fieldBuilder.append(appendValue(fieldDescription.getFieldClassRepresentation(), methodName)).toString();
    }

    private static String appendValue(ClassRepresentation classRepresentation, String methodName) {
        ClassType classType = classRepresentation.getType();
        Class<?> clazz = classRepresentation.getObjectClass();

        if (classType == PRIMITIVE_OR_WRAPPER || classType == STRING) {
            return "{{builderName}}.append(" + getSimpleValue(classType, methodName) + ");\n\t\t";
        }

        String complexParameterClass = clazz.getCanonicalName();
        String complexVarName = clazz.getSimpleName().toLowerCase() + getId();
        String createJsonGeneratorFunction = getCreateJsonGeneratorFunction(classType);
        StringBuilder collectionValueBuilder;

        collectionValueBuilder = new StringBuilder(
                complexParameterClass + " " + complexVarName + " = " + methodName + ";\n\t\t" +
                        "if (" + complexVarName + " == null) {\n\t\t\t"
                        + "{{builderName}}.append(\"null\");\n\t\t"
        );

        if (classType == COLLECTION) {
            collectionValueBuilder.append("} else if (").append(methodName).append(".isEmpty()) {\n\t\t\t")
                    .append("{{builderName}}.append(\"[]\");\n\t\t");
        }

        collectionValueBuilder.append("} else {\n\t\t\t").append("{{builderName}}.append(")
                .append(GeneratorFactory.class.getName()).append(createJsonGeneratorFunction).append(methodName)
                .append(").toJson(").append(complexVarName).append("));\n\t\t}\n\t\t");

        return collectionValueBuilder.toString();
    }

    @NotNull
    private static String getCreateJsonGeneratorFunction(ClassType type) {
        return type == COLLECTION ? CREATE_JSON_GENERATOR_FOR_COLLECTION : CREATE_JSON_GENERATOR;
    }

    private static String getSimpleValue(ClassType classType, String methodName) {
        StringBuilder fieldBuilder = new StringBuilder();
        if (classType == STRING) {
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

    @NotNull
    private static String getClassBeginning(String imports, String namePostfix, String clazzCanonicalName, String objectName) {
        return "package ru.sbt.reflection.parser.generator;\n" + imports +
                String.format("public class JsonGeneratorFor%s implements JsonGenerator<%s> {\n\t", namePostfix, clazzCanonicalName) +
                String.format("public String toJson(%s %s) {\n\t\t", clazzCanonicalName, objectName) +
                "StringBuilder {{builderName}} = new StringBuilder();\n\t\t";
    }

    private static int getId() {
        return random.nextInt(Integer.MAX_VALUE);
    }

}
