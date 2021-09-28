package ru.sbt.garage.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@ToString
public class Owner {
    private final long ownerId;
    private final String name;
    private final String lastName;
    private final int age;

    public static Owner createDummyOwner(long id) {
        return Owner.builder()
                .ownerId(id)
                .build();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return ownerId == owner.ownerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId);
    }
}
