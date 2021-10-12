package ru.sbt.clients;

import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class IndividualClient extends AbstractClient {
    private final String inn;

    public IndividualClient(String name, String inn) {
        super(ClientType.INDIVIDUAL, name);
        this.inn = inn;
    }

    public static IndividualClient parseFromJson(Map<String, Object> jsonStructure) {
        if (jsonStructure == null) {
            return null;
        }

        String name = (String) jsonStructure.get("name");
        String inn = (String) jsonStructure.get("inn");

        return new IndividualClient(name, inn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndividualClient)) return false;
        IndividualClient that = (IndividualClient) o;
        return Objects.equals(inn, that.inn) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getClientType(), that.getClientType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn, getClientType(), getName());
    }

    @Override
    public String toString() {
        return "IndividualClient{" +
                "inn='" + inn + '\'' +
                ", clientType=" + getClientType() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
