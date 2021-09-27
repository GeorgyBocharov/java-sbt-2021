package ru.sbt.garage.entities.impl;

import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.Garage;
import ru.sbt.garage.entities.Owner;

import java.util.Collection;

public class GarageImpl implements Garage {


    // O(1)
    @Override
    public Collection<Owner> allCarsUniqueOwners() {
        return null;
    }

    @Override
    public Collection<Car> topThreeCarsByMaxVelocity() {
        return null;
    }

    // O(1)
    @Override
    public Collection<Car> allCarsOfBrand(String brand) {
        return null;
    }

    @Override
    public Collection<Car> carsWithPowerMoreThan(int power) {
        return null;
    }

    @Override
    public Collection<Car> allCarsOfOwner(Owner owner) {
       return null;
    }

    @Override
    public int meanOwnersAgeOfCarBrand(String brand) {
        return 0;
    }

    @Override
    public int meanCarNumberForEachOwner() {
        return 0;
    }

    @Override
    public Car removeCar(int carId) {
        return null;
    }

    @Override
    public void addCar(Car car, Owner owner) {

    }
}
