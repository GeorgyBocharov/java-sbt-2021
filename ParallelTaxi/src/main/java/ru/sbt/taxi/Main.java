package ru.sbt.taxi;

import ru.sbt.taxi.entities.Dispatcher;
import ru.sbt.taxi.entities.OrderCreationService;
import ru.sbt.taxi.entities.OrderExecutor;
import ru.sbt.taxi.entities.Taxi;
import ru.sbt.taxi.entities.impl.DispatcherImpl;
import ru.sbt.taxi.entities.impl.OrderCreationServiceImpl;
import ru.sbt.taxi.entities.impl.OrderExecutorImpl;
import ru.sbt.taxi.entities.impl.TaxiWithLock;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int ORDER_NUMBER = 300;
    public static final int TAXI_NUMBER = 10;

    public static void main(String[] args) {
        OrderCreationService orderCreationService = new OrderCreationServiceImpl(ORDER_NUMBER);
        Dispatcher dispatcher = new DispatcherImpl(orderCreationService);
        OrderExecutor orderExecutor = new OrderExecutorImpl();

        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setName("Dispatcher");
        List<Thread> taxiThreads = createdTaxiThreads(TAXI_NUMBER, dispatcher, orderExecutor);
        dispatcherThread.start();
        taxiThreads.forEach(Thread::start);


    }

    private static List<Thread> createdTaxiThreads(int number, Dispatcher dispatcher, OrderExecutor orderExecutor) {
        List<Thread> taxiThreads = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            taxiThreads.add(createTaxiThread(new TaxiWithLock(dispatcher, orderExecutor)));
        }
        return taxiThreads;
    }

    private static Thread createTaxiThread(Taxi taxi) {
        Thread t = new Thread(taxi);
        t.setName("Taxi" + taxi.hashCode() % 100);
        return t;
    }
}
