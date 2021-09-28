package ru.sbt.garage.entities;

import java.util.Objects;

public class CarIdPowerPair {
    private final long carId;
    private final int power;

    private CarIdPowerPair(long carId, int power) {
        this.carId = carId;
        this.power = power;
    }

    public static CarIdPowerPair of(long carId, int power) {
        return new CarIdPowerPair(carId, power);
    }

    public int getPower() {
        return power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarIdPowerPair)) return false;
        CarIdPowerPair that = (CarIdPowerPair) o;
        return carId == that.carId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId);
    }
}
