package ru.sbt.taxi.entities.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.sbt.taxi.entities.Dispatcher;
import ru.sbt.taxi.entities.OrderCreationService;
import ru.sbt.taxi.entities.Taxi;

import java.util.ArrayDeque;
import java.util.Queue;

@Getter
@RequiredArgsConstructor
@ToString
public class DispatcherImpl implements Dispatcher {
    private final Queue<Taxi> availableTaxiQueue = new ArrayDeque<>();
    private final Queue<Order> orderQueue = new ArrayDeque<>();
    private final OrderCreationService orderCreationService;


    @Override
    public void run() {
        Taxi availableTaxi;
        Order order;
        while (true) {
            synchronized (availableTaxiQueue) {
                if (waitForAvailableTaxi()) return;
                order = getNextOrder();
                if (order == null) {
                    System.out.println("[Dispatcher] Orders ended, skipping");
                    continue;
                }
                availableTaxi = availableTaxiQueue.poll();
            }
            availableTaxi.placeOrder(order);
        }
    }

    @Override
    public void notifyAvailable(Taxi taxi) {
        synchronized (availableTaxiQueue) {
            System.out.println("[Dispatcher] is adding taxi " + taxi.hashCode() + " to queue");
            availableTaxiQueue.add(taxi);
            availableTaxiQueue.notify();
        }
    }

    private boolean waitForAvailableTaxi() {
        while (availableTaxiQueue.isEmpty()) {
            try {
                System.out.println("[Dispatcher] is waiting for available cars");
                availableTaxiQueue.wait();
            } catch (InterruptedException iex) {
                iex.printStackTrace();
                return true;
            }
        }
        return false;
    }

    private Order getNextOrder() {
        if (orderQueue.isEmpty()) {
            orderQueue.addAll(orderCreationService.createOrders());
        }
        return orderQueue.poll();
    }

}
