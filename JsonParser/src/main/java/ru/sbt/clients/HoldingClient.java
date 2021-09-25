package ru.sbt.clients;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class HoldingClient extends AbstractClient {
    private String holdingName;
    private int holdingMemberNbr;
    private List<String> holdingMemberNames;

    public HoldingClient() {
        super(ClientType.HOLDING);
    }

    public static HoldingClient parseFromJson(Map<String, Object> jsonStructure) {

        HoldingClient holdingClient = new HoldingClient();

        holdingClient.name = (String) jsonStructure.get("name");
        holdingClient.holdingName = (String) jsonStructure.get("holdingName");
        holdingClient.holdingMemberNbr = Integer.parseInt((String) jsonStructure.get("holdingMemberNbr"));
        holdingClient.holdingMemberNames =  (List<String>) jsonStructure.get("holdingMemberNames");

        return holdingClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoldingClient)) return false;
        HoldingClient that = (HoldingClient) o;
        return holdingMemberNbr == that.holdingMemberNbr &&
                Objects.equals(holdingName, that.holdingName) &&
                Objects.equals(clientType, that.clientType) &&
                Objects.equals(name, that.name) &&
                Objects.equals(holdingMemberNames, that.holdingMemberNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holdingName, holdingMemberNbr, holdingMemberNames, clientType, name);
    }

    @Override
    public String toString() {
        return "HoldingClient{" +
                "holdingName='" + holdingName + '\'' +
                ", holdingMemberNbr=" + holdingMemberNbr +
                ", holdingMemberNames=" + holdingMemberNames +
                ", clientType=" + clientType +
                ", name='" + name + '\'' +
                '}';
    }
}
