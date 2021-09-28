package ru.sbr.garage.entities.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.Owner;
import ru.sbt.garage.entities.impl.GarageImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static utils.GarageTestUtils.bmwFirst;
import static utils.GarageTestUtils.bmwSecond;
import static utils.GarageTestUtils.firstOwner;
import static utils.GarageTestUtils.firstOwnerCars;
import static utils.GarageTestUtils.meanCarNumberPerOwner;
import static utils.GarageTestUtils.secondOwner;
import static utils.GarageTestUtils.secondOwnerCars;
import static utils.GarageTestUtils.toyota;
import static utils.GarageTestUtils.vaz;

public class GarageImplStatisticFunctionsTest {

    private static final GarageImpl garage = new GarageImpl();

    @BeforeAll
    public static void beforeAll() {
        firstOwnerCars.forEach(car -> garage.addCar(car, firstOwner));
        secondOwnerCars.forEach(car -> garage.addCar(car, secondOwner));
    }

    @Test
    public void allCarsUniqueOwnersTest() {
        Collection<Owner> owners = garage.allCarsUniqueOwners();
        Assertions.assertNotNull(owners);
        Assertions.assertEquals(2, owners.size());
        Assertions.assertTrue(owners.contains(firstOwner));
        Assertions.assertTrue(owners.contains(secondOwner));
    }

    @Test
    public void topThreeCarsByMaxVelocityTest() {
        Collection<Car> top3ByVelocity = garage.topThreeCarsByMaxVelocity();

        Assertions.assertNotNull(top3ByVelocity);
        Assertions.assertEquals(GarageImpl.VELOCITY_QUEUE_SIZE, top3ByVelocity.size());
        Assertions.assertTrue(top3ByVelocity.contains(bmwFirst));
        Assertions.assertTrue(top3ByVelocity.contains(bmwSecond));
        Assertions.assertTrue(top3ByVelocity.contains(toyota));
    }

    @Test
    public void meanCarNumberForEachOwnerTest() {
        int actual = garage.meanCarNumberForEachOwner();
        Assertions.assertEquals(meanCarNumberPerOwner, actual);
    }

    @ParameterizedTest
    @MethodSource("provideDataForCarsByBrandTest")
    public void allCarsOfBrandTest(String brand, List<Car> expectedCars) {
        Collection<Car> bmwCars = garage.allCarsOfBrand(brand);

        Assertions.assertNotNull(bmwCars);
        Assertions.assertEquals(expectedCars.size(), bmwCars.size());
        expectedCars.forEach(car -> Assertions.assertTrue(bmwCars.contains(car)));
    }

    @ParameterizedTest
    @MethodSource("provideDataForCarsByPowerTest")
    public void carsWithPowerMoreThanTest(int power, List<Car> expectedCars) {
        Collection<Car> carsByPower = garage.carsWithPowerMoreThan(power);
        Assertions.assertNotNull(carsByPower);
        Assertions.assertEquals(expectedCars.size(), carsByPower.size());
        expectedCars.forEach(car -> Assertions.assertTrue(carsByPower.contains(car)));
    }

    @ParameterizedTest
    @MethodSource("provideDataForCarsByOwnersTest")
    public void allCarsOfOwnerTest(Owner owner, List<Car> expectedCars) {
        Collection<Car> carsByOwner = garage.allCarsOfOwner(owner);
        Assertions.assertNotNull(carsByOwner);
        Assertions.assertEquals(expectedCars.size(), carsByOwner.size());
        expectedCars.forEach(car -> Assertions.assertTrue(carsByOwner.contains(car)));
    }

    @ParameterizedTest
    @MethodSource("provideDataForMeanAgeByBrandTest")
    public void meanOwnersAgeOfCarBrandTest(String brand, int expected) {
        int actual = garage.meanOwnersAgeOfCarBrand(brand);
        Assertions.assertEquals(expected, actual);
    }


    private static Stream<Arguments> provideDataForMeanAgeByBrandTest() {
        return Stream.of(
                Arguments.of(bmwFirst.getBrand(), firstOwner.getAge()),
                Arguments.of(vaz.getBrand(), secondOwner.getAge()),
                Arguments.of(toyota.getBrand(), secondOwner.getAge())
        );
    }

    private static Stream<Arguments> provideDataForCarsByOwnersTest() {
        return Stream.of(
                Arguments.of(firstOwner, List.of(bmwFirst, bmwSecond)),
                Arguments.of(secondOwner, List.of(vaz, toyota))
        );
    }

    private static Stream<Arguments> provideDataForCarsByPowerTest() {
        return Stream.of(
                Arguments.of(bmwSecond.getPower() - 1, List.of(bmwFirst, bmwSecond, toyota)),
                Arguments.of(vaz.getPower(), List.of(bmwFirst, bmwSecond, toyota)),
                Arguments.of(vaz.getPower() - 1, List.of(vaz, bmwFirst, bmwSecond, toyota))
        );
    }

    private static Stream<Arguments> provideDataForCarsByBrandTest() {
        return Stream.of(
                Arguments.of(bmwFirst.getBrand(), List.of(bmwFirst, bmwSecond)),
                Arguments.of(vaz.getBrand(), List.of(vaz)),
                Arguments.of(toyota.getBrand(), List.of(toyota))
        );
    }
}
