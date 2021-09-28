package utils;

import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.Owner;

import java.util.List;

public final class GarageTestUtils {

    public static final int meanCarNumberPerOwner = 2;

    public static final Owner firstOwner = Owner.builder().ownerId(1).age(30).name("name").lastName("lastName").build();
    public static final Owner secondOwner = Owner.builder().ownerId(2).age(25).name("name2").lastName("lastName2").build();

    public static final Car bmwFirst = Car.builder().carId(1).brand("BMW").modelName("GT").power(500).maxVelocity(270).ownerId(firstOwner.getOwnerId()).build();
    public static final Car bmwSecond = Car.builder().carId(2).brand("BMW").modelName("M3").power(190).maxVelocity(238).ownerId(firstOwner.getOwnerId()).build();
    public static final Car vaz = Car.builder().carId(3).brand("VAZ").modelName("2109").power(78).maxVelocity(155).ownerId(secondOwner.getOwnerId()).build();
    public static final Car toyota = Car.builder().carId(4).brand("TOYOTA").modelName("CAMRY 3.5").power(249).maxVelocity(220).ownerId(secondOwner.getOwnerId()).build();

    public static final List<Car> firstOwnerCars = List.of(bmwFirst, bmwSecond);

    public static final List<Car> secondOwnerCars = List.of(vaz, toyota);

    private GarageTestUtils() {

    }
}
