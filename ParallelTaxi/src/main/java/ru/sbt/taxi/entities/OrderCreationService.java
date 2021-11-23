package ru.sbt.taxi.entities;

import ru.sbt.taxi.entities.impl.Order;

import java.util.List;

public interface OrderCreationService {
    List<Order> createOrders();
}
