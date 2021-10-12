package ru.sbt.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractClient {
    private final ClientType clientType;
    private final String name;

}
