package ru.sbt.service;

import ru.sbt.clients.AbstractClient;

import java.util.Optional;

public interface ClientParsingService {
    Optional<AbstractClient> parseClientFromJson(String jsonString);
}
