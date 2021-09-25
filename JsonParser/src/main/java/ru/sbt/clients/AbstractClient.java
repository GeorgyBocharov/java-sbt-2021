package ru.sbt.clients;

import lombok.Getter;

@Getter
public abstract class AbstractClient {
    protected ClientType clientType;
    protected String name;

    public AbstractClient(ClientType clientType) {
        this.clientType = clientType;
    }

    public void setName(String name) {
        this.name = name;
    }
}
