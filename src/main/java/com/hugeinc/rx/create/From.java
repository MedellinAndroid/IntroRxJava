package com.hugeinc.rx.create;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * User: HUGE-gilbert
 * Date: 12/13/16
 * Time: 12:35 PM
 */
public class From {
    public void exampleFromFuture() {
        FutureTask<Integer> f = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return 21;
        });
        new Thread(f).start();

        Observable<Integer> values = Observable.from(f);

        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Received: 21
        // Completed
    }

    public void exampleFromFutureTimeout() {
        FutureTask<Integer> f = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return 21;
        });
        new Thread(f).start();

        Observable<Integer> values = Observable.from(f, 1000, TimeUnit.MILLISECONDS);

        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Error: java.util.concurrent.TimeoutException
    }

    public void exampleFromArray() {
        Integer[] is = {1,2,3};
        Observable<Integer> values = Observable.from(is);
        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Received: 1
        // Received: 2
        // Received: 3
        // Completed
    }

    public void exampleFromIterable() {
        Iterable<Integer> input = Arrays.asList(1,2,3);
        Observable<Integer> values = Observable.from(input);
        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Received: 1
        // Received: 2
        // Received: 3
        // Completed
    }


    //
    // Tests
    //

    @Test
    public void testFromFuture() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        FutureTask<Integer> f = new FutureTask<>(() -> 21);
        new Thread(f).start();

        Observable<Integer> values = Observable.from(f);

        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(21));
        tester.assertNoErrors();
        tester.assertTerminalEvent();
    }

    @Test
    public void testFromArray() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Integer[] input = {1,2,3};
        Observable<Integer> values = Observable.from(input);
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(input));
        tester.assertNoErrors();
        tester.assertTerminalEvent();
    }

    @Test
    public void testFromIterable() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Iterable<Integer> input = Arrays.asList(1,2,3);
        Observable<Integer> values = Observable.from(input);
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(1,2,3));
        tester.assertNoErrors();
        tester.assertTerminalEvent();
    }

    public static void main(String[] args) {
        new From().exampleFromIterable();
    }
}
