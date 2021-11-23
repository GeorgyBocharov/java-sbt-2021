package ru.sbt.taxi.entities.impl;

import ru.sbt.taxi.entities.Dispatcher;
import ru.sbt.taxi.entities.OrderExecutor;
import ru.sbt.taxi.entities.Taxi;

import java.util.ArrayList;
import java.util.List;

public class TaxiWithLock implements Taxi {

    private final Dispatcher dispatcher;
    private final OrderExecutor orderExecutor;
    private final Object lock = new Object();
    private final List<Order> executedOrders = new ArrayList<>();
    private Order activeOrder;

    public TaxiWithLock(Dispatcher dispatcher, OrderExecutor orderExecutor) {
        this.dispatcher = dispatcher;
        this.orderExecutor = orderExecutor;
        notifyDispatcher();
    }

    @Override
    public void run() {
        Order localCurrentOrder;
        while (true) {
            synchronized (lock) {
                if (waitOrderAssignment()) {
                    return;
                }
                localCurrentOrder = activeOrder;
                activeOrder = null;
            }

            System.out.printf("Taxi %d is starting execution of order %s%n", hashCode(), localCurrentOrder);
            if (orderExecutor.executeOrder(localCurrentOrder)) return;

            executedOrders.add(localCurrentOrder);
            System.out.printf("Taxi %d finished execution of order%n", hashCode());
            notifyDispatcher();
        }
    }

    @Override
    public void placeOrder(Order order) {
        synchronized (lock) {
            System.out.printf("Taxi %d: Placed order%n", hashCode());
            activeOrder = order;
            lock.notify();
        }
    }

    @Override
    public List<Order> getExecutedOrders() {
        return executedOrders;
    }

    private boolean waitOrderAssignment() {
        while (activeOrder == null) {
            System.out.printf("Taxi %s is waiting for order's assignment%n", hashCode());
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return true;
            }
        }
        return false;
    }

    private void notifyDispatcher() {
        System.out.printf("Taxi %d is notifying dispatcher%n", hashCode());
        synchronized (dispatcher) {
            dispatcher.notifyAvailable(this);
        }
    }

}
