package com.hugeinc.rx.create;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * User: HUGE-gilbert
 * Date: 12/13/16
 * Time: 12:17 PM
 */
public class SimpleFactoryMethods {

    public void exampleJust() {
        Observable<String> values = Observable.just("one", "two", "three");
        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Received: one
        // Received: two
        // Received: three
        // Completed
    }

    public void exampleEmpty() {
        Observable<String> values = Observable.empty();
        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Completed
    }

    public void exampleNever() {
        Observable<String> values = Observable.never();
        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );
    }

    public void exampleError() {
        Observable<String> values = Observable.error(new Exception("Oops"));
        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Error: java.lang.Exception: Oops
    }

    public void exampleShouldDefer() {
        Observable<Long> now = Observable.just(System.currentTimeMillis());

        now.subscribe(System.out::println);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        now.subscribe(System.out::println);

        // 1431443908375
        // 1431443908375
    }

    public void exampleDefer() {
        Observable<Long> now = Observable.defer(() ->
                Observable.just(System.currentTimeMillis()));

        now.subscribe(System.out::println);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        now.subscribe(System.out::println);

        // 1431444107854
        // 1431444108858
    }

    public void exampleCreate() {
        Observable<String> values = Observable.create(o -> {
            o.onNext("Hello");
            o.onCompleted();
        });

        values.subscribe(
                v -> System.out.println("Received: " + v),
                e -> System.out.println("Error: " + e),
                () -> System.out.println("Completed")
        );

        // Received: Hello
        // Completed
    }


    //
    // Tests
    //

    @Test
    public void testJust() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.just("one", "two", "three");
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList("one", "two", "three"));
        tester.assertNoErrors();
        tester.assertTerminalEvent();
    }

    @Test
    public void testEmpty() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.empty();
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList());
        tester.assertNoErrors();
        tester.assertTerminalEvent();
    }

    @Test
    public void testNever() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.never();
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList());
        tester.assertNoErrors();
        assertEquals(tester.getOnCompletedEvents().size(), 0);
    }

    @Test
    public void testError() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.error(new Exception("Oops"));
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList());
        tester.assertTerminalEvent();
        assertEquals(tester.getOnErrorEvents().size(), 1);
        assertEquals(tester.getOnCompletedEvents().size(), 0);
    }

    @Test
    public void testShouldDefer() throws InterruptedException {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester1 = new TestSubscriber<>();
        TestSubscriber<Long> tester2 = new TestSubscriber<>();

        Observable<Long> now = Observable.just(scheduler.now());

        now.subscribe(tester1);
        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS);
        now.subscribe(tester2);

        assertEquals(tester1.getOnNextEvents().get(0),
                tester2.getOnNextEvents().get(0));
    }

    @Test
    public void testDefer() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester1 = new TestSubscriber<>();
        TestSubscriber<Long> tester2 = new TestSubscriber<>();

        Observable<Long> now = Observable.defer(() ->
                Observable.just(scheduler.now()));

        now.subscribe(tester1);
        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS);
        now.subscribe(tester2);

        assertTrue(tester1.getOnNextEvents().get(0) <
                tester2.getOnNextEvents().get(0));
    }

    @Test
    public void testCreate() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.create(o -> {
            o.onNext("Hello");
            o.onCompleted();
        });
        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList("Hello"));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new SimpleFactoryMethods().exampleDefer();
    }
}
