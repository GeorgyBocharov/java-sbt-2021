package ru.sbt.clients;

import lombok.Getter;
import lombok.Setter;

import ru.sbt.parser.JsonParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class LegalEntityClient extends AbstractClient {
    private boolean isForeign;
    private List<IndividualClient> owners;

    public LegalEntityClient() {
        super(ClientType.LEGAL_ENTITY);
    }

    public static LegalEntityClient parseFromJson(Map<String, Object> jsonStructure) {
        if (jsonStructure == null) {
            return null;
        }

        LegalEntityClient legalEntityClient = new LegalEntityClient();

        legalEntityClient.name = (String) jsonStructure.get("name");
        legalEntityClient.isForeign =  Boolean.parseBoolean("isForeign");

        legalEntityClient.owners = JsonParser.parseObjectToObjectList(jsonStructure.get("owners"))
                .stream()
                .map(IndividualClient::parseFromJson)
                .collect(Collectors.toList());

        return legalEntityClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LegalEntityClient)) return false;
        LegalEntityClient that = (LegalEntityClient) o;
        return isForeign == that.isForeign &&
                Objects.equals(owners, that.owners) &&
                Objects.equals(clientType, that.clientType) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isForeign, owners, clientType, name);
    }

    @Override
    public String toString() {
        return "LegalEntityClient{" +
                "isForeign=" + isForeign +
                ", owners=" + owners +
                ", clientType=" + clientType +
                ", name='" + name + '\'' +
                '}';
    }
}
