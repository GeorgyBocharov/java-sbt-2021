package ru.sbt.taxi.entities;

public interface Dispatcher extends Runnable {
    void notifyAvailable(Taxi taxi);
}
