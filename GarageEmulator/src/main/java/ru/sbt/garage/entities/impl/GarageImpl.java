package ru.sbt.garage.entities.impl;

import lombok.Getter;
import lombok.ToString;
import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.Garage;
import ru.sbt.garage.entities.Owner;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Getter
@ToString
public class GarageImpl implements Garage {

    public static final int VELOCITY_QUEUE_SIZE = 3;

    private static final int UNKNOWN_CAR_ID = -1;

    private final Map<String, Set<Car>> brandToCars = new HashMap<>();
    private final Map<Owner, Set<Car>> ownerToCars = new HashMap<>();
    private final Map<Long, Car> carMap = new HashMap<>();

    private final TreeSet<Car> carsByPower = new TreeSet<>(
            comparing(Car::getPower)
                    .thenComparing(comparing(Car::getCarId).reversed())
    );

    private final Queue<Car> carsByMaxVelocity = new PriorityQueue<>(
            VELOCITY_QUEUE_SIZE,
            comparing(Car::getMaxVelocity)
    );


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
        return carsByPower.tailSet(Car.builder().carId(UNKNOWN_CAR_ID).power(power).build(), false);
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

        float averageAge = (float) ownerToCars.keySet()
                .stream()
                .filter(owner -> ownerIds.contains(owner.getOwnerId()))
                .mapToInt(Owner::getAge)
                .average()
                .orElse(0);

        return Math.round(averageAge);
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

        removeFromFeatureMap(brandToCars, Car::getBrand, carToRemove);
        removeFromFeatureMap(ownerToCars, ((Function<Car, Long>) Car::getOwnerId).andThen(Owner::createDummyOwner), carToRemove);

        carsByMaxVelocity.remove(carToRemove);
        return carToRemove;
    }

    @Override
    public void addCar(Car car, Owner owner) {
        carMap.put(car.getCarId(), car);
        carsByPower.add(car);

        addToFeatureMap(brandToCars, car.getBrand(), car);
        addToFeatureMap(ownerToCars, owner, car);

        addToCarsByMaxVelocity(car);
    }

    private void addToCarsByMaxVelocity(Car car) {
        if (carsByMaxVelocity.size() < VELOCITY_QUEUE_SIZE) {
            carsByMaxVelocity.add(car);
        } else if (car.getMaxVelocity() > carsByMaxVelocity.peek().getMaxVelocity()) {
            carsByMaxVelocity.poll();
            carsByMaxVelocity.add(car);
        }
    }

    private static <R, T> void addToFeatureMap(Map<R, Set<T>> featureMap, R feature, T elem) {
        if (feature == null) {
            return;
        }
        featureMap.putIfAbsent(feature, new HashSet<>());
        featureMap.get(feature).add(elem);
    }

    private static <R, T> void removeFromFeatureMap(Map<R, Set<T>> featureMap, Function<T, R> featureGetter, T elem) {
        R feature = featureGetter.apply(elem);
        if (feature == null) {
            return;
        }
        Set<T> carsByBrand = featureMap.get(feature);
        if (carsByBrand != null) {
            carsByBrand.remove(elem);
            if (carsByBrand.isEmpty()) {
                featureMap.remove(feature);
            }
        }
    }


}
