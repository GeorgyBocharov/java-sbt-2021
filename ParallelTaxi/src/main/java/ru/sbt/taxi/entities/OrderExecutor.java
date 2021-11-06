package ru.sbt.taxi.entities;

import ru.sbt.taxi.entities.impl.Order;

public interface OrderExecutor {
    boolean executeOrder(Order order);
}
