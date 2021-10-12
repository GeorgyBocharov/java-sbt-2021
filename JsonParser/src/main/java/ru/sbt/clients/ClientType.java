package ru.sbt.clients;

import ru.sbt.parser.JsonParser;

public enum ClientType {
    INDIVIDUAL {
        public AbstractClient parseClientFromJsonString(String jsonString) {
            return IndividualClient.parseFromJson(JsonParser.parseJsonStringToMap(jsonString));
        }
    },
    LEGAL_ENTITY {
        public AbstractClient parseClientFromJsonString(String jsonString) {
            return LegalEntityClient.parseFromJson(JsonParser.parseJsonStringToMap(jsonString));
        }
    },
    HOLDING {
        public AbstractClient parseClientFromJsonString(String jsonString) {
            return HoldingClient.parseFromJson(JsonParser.parseJsonStringToMap(jsonString));
        }
    };

    public abstract AbstractClient parseClientFromJsonString(String jsonString);
}
