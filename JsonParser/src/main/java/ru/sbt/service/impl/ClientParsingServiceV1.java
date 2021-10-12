package ru.sbt.service.impl;

import ru.sbt.clients.AbstractClient;
import ru.sbt.clients.ClientType;

import java.util.Optional;

public class ClientParsingServiceV1 extends AbstractClientParsingService {

    @Override
    public Optional<AbstractClient> parseClientFromJson(String jsonString) {
        String clientTypeString = getClientTypeString(jsonString);

        return Optional.ofNullable(
                ClientType
                        .valueOf(clientTypeString)
                        .parseClientFromJsonString(jsonString)
        );
    }
}
