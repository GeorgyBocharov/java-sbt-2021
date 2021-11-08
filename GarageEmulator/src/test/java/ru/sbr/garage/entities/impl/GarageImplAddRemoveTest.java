package ru.sbr.garage.entities.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sbt.garage.entities.Car;
import ru.sbt.garage.entities.impl.GarageImpl;

import static utils.GarageTestUtils.bmwFirst;
import static utils.GarageTestUtils.firstOwner;

public class GarageImplAddRemoveTest {


    private GarageImpl garage;
    private Car car;

    @BeforeEach
    public void recreateGarage() {
        garage = new GarageImpl();
        car = bmwFirst;
        garage.addCar(car, firstOwner);
    }

    @Test
    public void testAddCar() {

        Assertions.assertEquals(car, garage.getCarsByMaxVelocity().peek());
        Assertions.assertTrue(garage.getBrandToCars().get(car.getBrand()).contains(car));
        Assertions.assertTrue(garage.getOwnerToCars().get(firstOwner).contains(car));
        Assertions.assertTrue(garage.getCarsByPower().contains(car));
        Assertions.assertTrue(garage.getCarMap().containsKey(car.getCarId()));
        Assertions.assertTrue(garage.getCarMap().containsValue(car));
    }

    @Test
    public void testRemoveCar() {

        Car removedCar = garage.removeCar(car.getCarId());
        Assertions.assertNotNull(removedCar);
        Assertions.assertEquals(car, removedCar);

        System.out.println(garage);
        Assertions.assertTrue(garage.getCarsByMaxVelocity().isEmpty());
        Assertions.assertTrue(garage.getBrandToCars().isEmpty());
        Assertions.assertTrue(garage.getOwnerToCars().isEmpty());
        Assertions.assertTrue(garage.getCarsByPower().isEmpty());
        Assertions.assertTrue(garage.getCarMap().isEmpty());
    }




}
