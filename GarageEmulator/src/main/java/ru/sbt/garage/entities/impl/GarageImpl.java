package ru.sbt.garage.entities.impl;

import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.CarIdPowerPair;
import ru.sbt.garage.entities.Garage;
import ru.sbt.garage.entities.Owner;

import java.util.*;

public class GarageImpl implements Garage {

    private static final int VELOCITY_QUEUE_SIZE = 3;

    Map<String, Set<Car>> brandToCars = new HashMap<>();
    Map<Long, Set<Car>> ownerToCars = new HashMap<>();
    TreeMap<CarIdPowerPair, Car> carsByPower = new TreeMap<>(Comparator.comparing(CarIdPowerPair::getPower));
    Queue<Car> carsByMaxVelocity = new PriorityQueue<>(VELOCITY_QUEUE_SIZE, Comparator.comparing(Car::getMaxVelocity));



    @Override
    public Collection<Owner> allCarsUniqueOwners() {
        return ownerToCars.keySet();
    }

    @Override
    public Collection<Car> topThreeCarsByMaxVelocity() {
        return carsByMaxVelocity;
    }

    // O(1)
    @Override
    public Collection<Car> allCarsOfBrand(String brand) {
        return brandToCars.get(brand);
    }

    @Override
    public Collection<Car> carsWithPowerMoreThan(int power) {
        return carsByPower.headMap(CarIdPowerPair.of(-1, power)).values();
    }

    // O(1)
    @Override
    public Collection<Car> allCarsOfOwner(Owner owner) {
       return ownerToCars.get(owner.getOwnerId());
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
        Car carToRemove = carsByPower.remove(CarIdPowerPair.of(carId, 0));
        if (carToRemove == null) {
            return null;
        }
        brandToCars.get(carToRemove.getBrand()).remove(carToRemove);
        ownerToCars.get(carToRemove.)

    }

    @Override
    public void addCar(Car car, Owner owner) {
        brandToCars.putIfAbsent(car.getModelName(), new HashSet<>());
        ownerToCars.putIfAbsent(owner, new HashSet<>());

        brandToCars.get(car.getModelName()).add(car);
        ownerToCars.get(owner).add(car);

        carsByPower.put(CarIdPowerPair.of(car.getCarId(), car.getPower()), car);

        if (carsByMaxVelocity.size() < VELOCITY_QUEUE_SIZE) {
            carsByMaxVelocity.add(car);
        } else if (car.getMaxVelocity() > carsByMaxVelocity.peek().getMaxVelocity()) {
            carsByMaxVelocity.poll();
            carsByMaxVelocity.add(car);
        }
    }



}
