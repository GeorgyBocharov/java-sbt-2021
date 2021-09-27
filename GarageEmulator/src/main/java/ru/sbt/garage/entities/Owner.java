package ru.sbt.garage.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Owner {
    private final long ownerId;
    private final String name;
    private final String lastName;
    private final int age;
}
