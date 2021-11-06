package ru.sbt.taxi.entities.impl;

import lombok.ToString;
import ru.sbt.taxi.entities.Dispatcher;
import ru.sbt.taxi.entities.OrderExecutor;
import ru.sbt.taxi.entities.Taxi;

import java.util.ArrayList;
import java.util.List;

@ToString
public class TaxiWithBusyWait implements Taxi {

    private final Dispatcher dispatcher;
    private final OrderExecutor orderExecutor;
    private volatile Order activeOrder;
    private final List<Order> executedOrders = new ArrayList<>();

    public TaxiWithBusyWait(Dispatcher dispatcher, OrderExecutor orderExecutor) {
        this.dispatcher = dispatcher;
        this.orderExecutor = orderExecutor;
        notifyDispatcher();
    }

    @Override
    public void run() {
        while (true) {
            if (activeOrder == null) {
                continue;
            }

            System.out.printf("Taxi %d is starting execution of order %s%n", hashCode(), activeOrder);
            if (orderExecutor.executeOrder(activeOrder)) return;

            executedOrders.add(activeOrder);
            activeOrder = null;
            System.out.printf("Taxi %d finished execution of order%n", hashCode());
            notifyDispatcher();
        }
    }

    @Override
    public void placeOrder(Order order) {
        System.out.printf("Taxi %d: Placed order%n", hashCode());
        activeOrder = order;
    }

    @Override
    public List<Order> getExecutedOrders() {
        return executedOrders;
    }

    private void notifyDispatcher() {
        System.out.printf("Taxi %d is notifying dispatcher%n", hashCode());
        synchronized (dispatcher) {
            dispatcher.notifyAvailable(this);
        }
    }

}
