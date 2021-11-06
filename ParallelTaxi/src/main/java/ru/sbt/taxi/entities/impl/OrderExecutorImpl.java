package ru.sbt.taxi.entities.impl;

import ru.sbt.taxi.entities.OrderExecutor;

public class OrderExecutorImpl implements OrderExecutor {

    @Override
    public boolean executeOrder(Order order) {
        try {
            Thread.sleep(order.getDuration());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return true;
        }
        return false;
    }
}
