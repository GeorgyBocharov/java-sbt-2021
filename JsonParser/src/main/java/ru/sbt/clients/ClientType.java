package ru.sbt.clients;

import ru.sbt.parser.JsonParser;

import java.util.Map;
import java.util.function.Function;

public enum ClientType {
    INDIVIDUAL(IndividualClient::parseFromJson),
    LEGAL_ENTITY(LegalEntityClient::parseFromJson),
    HOLDING(HoldingClient::parseFromJson);

    private final Function<Map<String, Object>, AbstractClient> creationFunction;

    ClientType(Function<Map<String, Object>, AbstractClient> creationFunction) {
        this.creationFunction = creationFunction;
    }

    public AbstractClient parseClientFromJsonString(String jsonString) {
        Map<String, Object> jsonStructure = JsonParser.parseJsonStringToMap(jsonString);
        return creationFunction.apply(jsonStructure);
    }
}
