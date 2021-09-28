package ru.sbt.garage.entities.impl;

import lombok.Getter;
import lombok.ToString;
import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.Garage;
import ru.sbt.garage.entities.Owner;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@ToString
public class GarageImpl implements Garage {

    public static final int VELOCITY_QUEUE_SIZE = 3;

    private final Map<String, Set<Car>> brandToCars = new HashMap<>();
    private final Map<Owner, Set<Car>> ownerToCars = new HashMap<>();
    private final Map<Long, Car> carMap = new HashMap<>();
    private final TreeSet<Car> carsByPower = new TreeSet<>(Comparator.comparing(Car::getPower));
    private final Queue<Car> carsByMaxVelocity = new PriorityQueue<>(VELOCITY_QUEUE_SIZE, Comparator.comparing(Car::getMaxVelocity));


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
        return carsByPower.tailSet(Car.builder().power(power).build(), false);
    }

    // O(1)
    @Override
    public Collection<Car> allCarsOfOwner(Owner owner) {
       return ownerToCars.get(owner);
    }

    @Override
    public int meanOwnersAgeOfCarBrand(String brand) {
        Set<Long> ownerIds = brandToCars.get(brand)
                .stream()
                .map(Car::getOwnerId)
                .collect(Collectors.toSet());

        Set<Integer> ownerAges = ownerToCars.keySet()
                .stream()
                .filter(owner -> ownerIds.contains(owner.getOwnerId())).map(Owner::getAge)
                .collect(Collectors.toSet());

        return Math.round((float) ownerAges.stream().reduce(Integer::sum).orElse(0) / ownerAges.size());
    }

    @Override
    public int meanCarNumberForEachOwner() {
        return Math.round((float) carsByPower.size() / ownerToCars.size());
    }

    @Override
    public Car removeCar(long carId) {
        Car carToRemove = carMap.remove(carId);
        if (carToRemove == null) {
            return null;
        }
        carsByPower.remove(carToRemove);

        brandToCars.get(carToRemove.getBrand()).remove(carToRemove);
        if (brandToCars.get(carToRemove.getBrand()).isEmpty()) {
            brandToCars.remove(carToRemove.getBrand());
        }

        Owner dummyOwner = Owner.createDummyOwner(carToRemove.getOwnerId());
        ownerToCars.get(dummyOwner).remove(carToRemove);
        if (ownerToCars.get(dummyOwner).isEmpty()) {
            ownerToCars.remove(dummyOwner);
        }

        carsByMaxVelocity.remove(carToRemove);
        return carToRemove;
    }

    @Override
    public void addCar(Car car, Owner owner) {
        carMap.put(car.getCarId(), car);
        brandToCars.putIfAbsent(car.getBrand(), new HashSet<>());
        ownerToCars.putIfAbsent(owner, new HashSet<>());

        brandToCars.get(car.getBrand()).add(car);
        ownerToCars.get(owner).add(car);

        carsByPower.add(car);

        if (carsByMaxVelocity.size() < VELOCITY_QUEUE_SIZE) {
            carsByMaxVelocity.add(car);
        } else if (car.getMaxVelocity() > carsByMaxVelocity.peek().getMaxVelocity()) {
            carsByMaxVelocity.poll();
            carsByMaxVelocity.add(car);
        }
    }



}
