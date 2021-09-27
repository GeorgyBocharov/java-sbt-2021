package ru.sbt.garage.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Car implements Comparable<Car> {
    private final long carId;
    private final String brand;
    private final String modelName;
    private final int maxVelocity;
    private final int power;
    private final int ownerId;


    @Override
    public int compareTo(Car other) {
        return Integer.compare(this.power, other.power);
    }
}
