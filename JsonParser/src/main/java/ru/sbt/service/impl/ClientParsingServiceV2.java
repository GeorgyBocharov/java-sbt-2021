package ru.sbt.service.impl;

import ru.sbt.clients.AbstractClient;
import ru.sbt.clients.HoldingClient;
import ru.sbt.clients.IndividualClient;
import ru.sbt.clients.LegalEntityClient;

import ru.sbt.parser.JsonParser;
import ru.sbt.service.error.ClientParsingException;

import java.util.Map;
import java.util.Optional;

public class ClientParsingServiceV2 extends AbstractClientParsingService {

    private static final String HOLDING = "HOLDING";
    private static final String INDIVIDUAL = "INDIVIDUAL";
    private static final String LEGAL_ENTITY = "LEGAL_ENTITY";

    @Override
    public Optional<AbstractClient> parseClientFromJson(String jsonString) {
        String clientTypeString = getClientTypeString(jsonString);

        AbstractClient abstractClient;
        Map<String, Object> jsonStructure = JsonParser.parseJsonStringToMap(jsonString);
        abstractClient = getAbstractClient(clientTypeString, jsonStructure);
        return Optional.ofNullable(abstractClient);
    }

    private AbstractClient getAbstractClient(String clientTypeString, Map<String, Object> jsonStructure) {
        AbstractClient abstractClient;
        switch (clientTypeString) {
            case HOLDING: {
                abstractClient = HoldingClient.parseFromJson(jsonStructure);
                break;
            }
            case INDIVIDUAL: {
                abstractClient = IndividualClient.parseFromJson(jsonStructure);
                break;
            }
            case LEGAL_ENTITY: {
                abstractClient = LegalEntityClient.parseFromJson(jsonStructure);
                break;
            }
            default: {
                throw new ClientParsingException("Failed to determine clientType");
            }
        }
        return abstractClient;
    }
}
