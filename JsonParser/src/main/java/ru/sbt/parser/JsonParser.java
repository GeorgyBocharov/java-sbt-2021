package ru.sbt.parser;

import ru.sbt.parser.error.EntityTypeException;
import ru.sbt.parser.error.JsonParsingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonParser {

    private JsonParser() {
    }

    public static List<Map<String, Object>> parseJsonToObjectList(String jsonString) {
        Object jsonStructure = getResultOfParsing(jsonString);
        return parseObjectToObjectList(jsonStructure);
    }

    public static List<Map<String, Object>> parseObjectToObjectList(Object jsonStructure) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (jsonStructure instanceof List) {
            List objects = (List) jsonStructure;
            if (objects.isEmpty()) {
                return result;
            }
            if (!(objects.get(0) instanceof Map)) {
                throw new EntityTypeException("Expected objects in list");
            }
            for (Object object : objects) {
                result.add((Map<String, Object>) object);
            }
            return result;
        } else if (jsonStructure == null) {
            return null;
        } else {
            throw new EntityTypeException("Expected List");
        }
    }

    public static Map<String, Object> parseJsonStringToMap(String jsonString) {
        Object object = getResultOfParsing(jsonString);
        if (object == null) {
            return null;
        } else if (object instanceof Map) {
            return (Map<String, Object>) object;
        } else {
            throw new EntityTypeException("Json entity isn't an object");
        }
    }

    private static Object getResultOfParsing(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        return parseJson(jsonString, 0).getResult();
    }

    private static JsonValue parseJson(String jsonString, int startIndex) {
        JsonValue jsonValue = new JsonValue();
        String elementName = null;
        for (int i = startIndex; i < jsonString.length(); i++) {
            switch (jsonString.charAt(i)) {
                case ' ':
                case '\r':
                case '\n':
                    break;
                case '[': {
                    ArrayList<Object> objects = new ArrayList<>();
                    jsonValue.setResult(objects);
                    JsonValue arrayElement = new JsonValue();
                    while (!arrayElement.isLastInArray()) {
                        if (i >= jsonString.length() - 1) {
                            throw new JsonParsingException("Failed to parse array");
                        }
                        arrayElement = parseJson(jsonString, i + 1);
                        i = arrayElement.getEndIndex();
                        if (arrayElement.getResult() != null) {
                            objects.add(arrayElement.getResult());
                        }
                    }

                    break;
                }
                case '{':
                    HashMap<String, Object> nameValueMap = new HashMap<>();
                    jsonValue.setResult(nameValueMap);
                    break;
                case '"': {
                    int endOfName = jsonString.indexOf('"', i + 1);
                    String stringValue = jsonString.substring(i + 1, endOfName);
                    i = endOfName;
                    if (jsonValue.getResult() instanceof Map) {
                        elementName = stringValue;
                    } else {
                        jsonValue.setResult(stringValue);
                    }
                    break;
                }
                case ':': {
                    if (!(jsonValue.getResult() instanceof Map) || elementName == null) {
                        throw new JsonParsingException("Unexpected ':'");
                    }
                    JsonValue value = parseJson(jsonString, i + 1);
                    ((Map) jsonValue.getResult()).put(elementName, value.getResult());
                    i = value.getEndIndex();
                    break;
                }
                case ']': {
                    jsonValue.setLastInArray(true);
                }
                case ',':
                case '}': {
                    jsonValue.setEndIndex(i);
                    Object resultValue = jsonValue.getResult();
                    if (resultValue instanceof StringBuilder) {
                        jsonValue.setResult(((StringBuilder) resultValue).toString());
                    }
                    return jsonValue;
                }

                default: {
                    if (jsonValue.getResult() == null) {
                        jsonValue.setResult(new StringBuilder());
                    } else if (!(jsonValue.getResult() instanceof StringBuilder)) {
                        throw new JsonParsingException("Expected StringBuilder value");
                    }
                    ((StringBuilder) jsonValue.getResult()).append(jsonString.charAt(i));
                    break;
                }
            }
        }
        return jsonValue;
    }

}
