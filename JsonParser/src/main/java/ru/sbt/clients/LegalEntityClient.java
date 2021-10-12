package ru.sbt.clients;

import lombok.Getter;

import ru.sbt.parser.JsonParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class LegalEntityClient extends AbstractClient {
    private final boolean isForeign;
    private final List<IndividualClient> owners;

    public LegalEntityClient(String name, boolean isForeign, List<IndividualClient> owners) {
        super(ClientType.LEGAL_ENTITY, name);
        this.isForeign = isForeign;
        this.owners = owners;
    }

    public static LegalEntityClient parseFromJson(Map<String, Object> jsonStructure) {
        if (jsonStructure == null) {
            return null;
        }

        String name = (String) jsonStructure.get("name");
        boolean isForeign =  Boolean.parseBoolean("isForeign");
        List<IndividualClient> owners = JsonParser.parseObjectToObjectList(jsonStructure.get("owners"))
                .stream()
                .map(IndividualClient::parseFromJson)
                .collect(Collectors.toList());

        return new LegalEntityClient(name, isForeign, owners);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LegalEntityClient)) return false;
        LegalEntityClient that = (LegalEntityClient) o;
        return isForeign == that.isForeign &&
                Objects.equals(owners, that.owners) &&
                Objects.equals(getClientType(), that.getClientType()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isForeign, owners, getClientType(), getName());
    }

    @Override
    public String toString() {
        return "LegalEntityClient{" +
                "isForeign=" + isForeign +
                ", owners=" + owners +
                ", clientType=" + getClientType() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
