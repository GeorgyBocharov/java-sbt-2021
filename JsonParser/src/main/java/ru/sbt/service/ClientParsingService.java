package ru.sbt.service;

import ru.sbt.clients.AbstractClient;

import java.util.Optional;
import java.util.regex.Pattern;

public interface ClientParsingService {

    Pattern clientTypePattern = Pattern.compile("\"clientType\":[ \n]*\"(.+)\"");

    Optional<AbstractClient> parseClientFromJson(String jsonString);
}
