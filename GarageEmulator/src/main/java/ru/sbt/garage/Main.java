package ru.sbt.garage;

import ru.sbt.garage.entities.Owner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
        Owner owner = Owner.createDummyOwner(1);
        System.out.println(owner);
    }
}
