package ru.sbt.parser;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.sbt.parser.error.EntityTypeException;
import ru.sbt.parser.error.JsonParsingException;
import utils.JsonTestFilesLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static utils.JsonTestFilesLoader.*;


public class JsonParserTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void parseJsonStringToMapTestSuccess(String fileName, Map<String, Object> expectedMap) throws IOException {
        String jsonString = JsonTestFilesLoader.loadJsonFile(fileName);
        Map<String, Object> actualMap = JsonParser.parseJsonStringToMap(jsonString);

        Assertions.assertNotNull(actualMap);
        Assertions.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void parseEmptyStringOrNullTest() {
        Assertions.assertNull(JsonParser.parseJsonStringToMap(null));
        Assertions.assertNull(JsonParser.parseJsonStringToMap(""));
    }

    @Test
    public void parseJsonStringToListTestSuccess() throws IOException {
        String jsonString = JsonTestFilesLoader.loadJsonFile(INDIVIDUAL_CLIENT_LIST_FILE);
        List<Map<String, Object>> actualList = JsonParser.parseJsonToObjectList(jsonString);
        List<Map<String, Object>> expectedList = List.of(
                Map.of(
                        "name", "George",
                        "inn", "12345325632",
                        "clientType", "INDIVIDUAL"
                ),
                Map.of(
                        "name", "Julia",
                        "inn", "12345325637",
                        "clientType", "INDIVIDUAL"
                )
        );

        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(expectedList, actualList);
    }


    @Test
    public void parseJsonStringToWrongTypeTest() {
        Assertions.assertThrows(
                EntityTypeException.class,
                () -> JsonParser.parseJsonStringToMap("[{\"name\": \"george\"}]")
        );
    }


    @Test
    public void parseJsonStringToMapTestFailure() {
        Assertions.assertThrows(
                JsonParsingException.class,
                () -> JsonParser.parseJsonStringToMap("{BAD_STRING}")
        );
    }


    public static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(
                        INDIVIDUAL_CLIENT_FILE,
                        Map.of(
                                "name", "George",
                                "inn", "12345325632",
                                "clientType", "INDIVIDUAL"
                        )),
                Arguments.of(
                        HOLDING_CLIENT_FILE,
                        Map.of(
                                "name", "Samokat",
                                "holdingName", "Sber",
                                "clientType", "HOLDING",
                                "holdingMemberNbr", "10",
                                "holdingMemberNames", List.of("LitRes", "PIK Group", "Sber Mobile", "2 Gis")
                        )
                )
        );
    }

}
