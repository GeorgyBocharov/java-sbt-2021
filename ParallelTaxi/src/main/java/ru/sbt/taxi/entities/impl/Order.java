package ru.sbt.taxi.entities.impl;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;
import java.util.UUID;

@Getter
@ToString
public class Order {

    private static final int MAX_ORDER_DURATION_MS = 5000;

    private final UUID id;
    private final String start;
    private final String end;
    private final int duration;
    private final Random random = new Random();

    public Order(String start, String end) {
        id = UUID.randomUUID();
        this.start = start;
        this.end = end;
        this.duration = random.nextInt(MAX_ORDER_DURATION_MS);
    }
}
