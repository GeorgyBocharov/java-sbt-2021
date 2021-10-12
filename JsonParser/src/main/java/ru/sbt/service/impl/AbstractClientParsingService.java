package ru.sbt.service.impl;

import ru.sbt.service.ClientParsingService;
import ru.sbt.service.error.ClientParsingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractClientParsingService implements ClientParsingService {

    private static final Pattern clientTypePattern = Pattern.compile("\"clientType\":[ \n]*\"(.+)\"");

    protected String getClientTypeString(String jsonString) {
        Matcher matcher = clientTypePattern.matcher(jsonString);
        if (!matcher.find()) {
            throw new ClientParsingException("Failed to find clientType tag in json string");
        }
        return matcher.group(1);
    }
}
