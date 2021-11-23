package ru.sbt.taxi.entities.impl;

import lombok.RequiredArgsConstructor;
import ru.sbt.taxi.entities.OrderCreationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class OrderCreationServiceImpl implements OrderCreationService {

    private static final int BOUND = 'Z' - 'A';

    private final Random random = new Random();
    private final int orderNumber;

    @Override
    public List<Order> createOrders() {
        List<Order> ordersBatch = new ArrayList<>(orderNumber);
        for (int i = 0; i < orderNumber; i++) {
            ordersBatch.add(createOrder());
        }
        return ordersBatch;
    }

    private Order createOrder() {
        int start = getRandomPlace();
        int end =  getRandomPlace();
        end = start == end ? (char) ((end + 1) % BOUND) : end;
        return new Order(String.valueOf((char) start), String.valueOf((char) end));
    }

    private int getRandomPlace() {
        return 'A' + random.nextInt(BOUND);
    }
}
