package ru.sbt.clients;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class IndividualClient extends AbstractClient {
    private String inn;

    public IndividualClient() {
        super(ClientType.INDIVIDUAL);
    }

    public static IndividualClient parseFromJson(Map<String, Object> jsonStructure) {

        IndividualClient individualClient = new IndividualClient();

        individualClient.name = (String) jsonStructure.get("name");
        individualClient.inn = (String) jsonStructure.get("inn");

        return individualClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndividualClient)) return false;
        IndividualClient that = (IndividualClient) o;
        return Objects.equals(inn, that.inn) &&
                Objects.equals(name, that.name) &&
                Objects.equals(clientType, that.clientType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn, clientType, name);
    }

    @Override
    public String toString() {
        return "IndividualClient{" +
                "inn='" + inn + '\'' +
                ", clientType=" + clientType +
                ", name='" + name + '\'' +
                '}';
    }
}
