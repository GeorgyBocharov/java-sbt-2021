package ru.sbt.clients;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class HoldingClient extends AbstractClient {
    private final String holdingName;
    private final int holdingMemberNbr;
    private final List<String> holdingMemberNames;

    public HoldingClient(String name, String holdingName, int holdingMemberNbr, List<String> holdingMemberNames) {
        super(ClientType.HOLDING, name);
        this.holdingName = holdingName;
        this.holdingMemberNbr = holdingMemberNbr;
        this.holdingMemberNames = holdingMemberNames;
    }

    public static HoldingClient parseFromJson(Map<String, Object> jsonStructure) {
        if (jsonStructure == null) {
            return null;
        }

        String name = (String) jsonStructure.get("name");
        String holdingName = (String) jsonStructure.get("holdingName");
        int holdingMemberNbr = Integer.parseInt((String) jsonStructure.get("holdingMemberNbr"));
        List<String> holdingMemberNames =  (List<String>) jsonStructure.get("holdingMemberNames");

        return new HoldingClient(name, holdingName, holdingMemberNbr, holdingMemberNames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoldingClient)) return false;
        HoldingClient that = (HoldingClient) o;
        return holdingMemberNbr == that.holdingMemberNbr &&
                Objects.equals(holdingName, that.holdingName) &&
                Objects.equals(getClientType(), that.getClientType()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(holdingMemberNames, that.holdingMemberNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holdingName, holdingMemberNbr, holdingMemberNames, getClientType(), getName());
    }

    @Override
    public String toString() {
        return "HoldingClient{" +
                "holdingName='" + holdingName + '\'' +
                ", holdingMemberNbr=" + holdingMemberNbr +
                ", holdingMemberNames=" + holdingMemberNames +
                ", clientType=" + getClientType() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
