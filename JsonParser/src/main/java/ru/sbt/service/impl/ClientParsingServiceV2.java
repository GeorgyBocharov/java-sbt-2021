package ru.sbt.service.impl;

import ru.sbt.clients.AbstractClient;
import ru.sbt.clients.HoldingClient;
import ru.sbt.clients.IndividualClient;
import ru.sbt.clients.LegalEntityClient;
import ru.sbt.parser.JsonParser;
import ru.sbt.service.ClientParsingService;
import ru.sbt.service.error.ClientParsingException;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

public class ClientParsingServiceV2 implements ClientParsingService {

    private static final String HOLDING = "HOLDING";
    private static final String INDIVIDUAL = "INDIVIDUAL";
    private static final String LEGAL_ENTITY = "LEGAL_ENTITY";

    @Override
    public Optional<AbstractClient> parseClientFromJson(String jsonString) {
        Matcher matcher = clientTypePattern.matcher(jsonString);
        String clientTypeString = null;
        if (matcher.find()) {
            clientTypeString = matcher.group(1);
        } else {
            throw new ClientParsingException("Failed to find clientType tag in json string");
        }

        AbstractClient abstractClient;
        Map<String, Object> jsonStructure = JsonParser.parseJsonStringToMap(jsonString);
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
        return abstractClient != null ? Optional.of(abstractClient) : Optional.empty();
    }
}
