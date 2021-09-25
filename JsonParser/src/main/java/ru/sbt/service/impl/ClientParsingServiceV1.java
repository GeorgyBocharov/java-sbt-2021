package ru.sbt.service.impl;

import ru.sbt.clients.AbstractClient;
import ru.sbt.clients.ClientType;
import ru.sbt.service.ClientParsingService;
import ru.sbt.service.error.ClientParsingException;

import java.util.Optional;
import java.util.regex.Matcher;

public class ClientParsingServiceV1 implements ClientParsingService {

    @Override
    public Optional<AbstractClient> parseClientFromJson(String jsonString) {
        Matcher matcher = clientTypePattern.matcher(jsonString);
        AbstractClient abstractClient;
        if (matcher.find()) {
            abstractClient = ClientType.valueOf(matcher.group(1)).parseClientFromJsonString(jsonString);
        } else {
            throw new ClientParsingException("Failed to find clientType tag in json string");
        }
        if (abstractClient == null) {
            return Optional.empty();
        }
        return Optional.of(abstractClient);
    }
}
