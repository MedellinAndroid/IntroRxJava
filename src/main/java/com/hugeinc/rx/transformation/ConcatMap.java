package com.hugeinc.rx.transformation;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 6:04 PM
 */
public class ConcatMap {
    public void exampleConcatMap() {
        Observable.just(100, 150)
                .concatMap(i ->
                        Observable.interval(i, TimeUnit.MILLISECONDS)
                                .map(v -> i)
                                .take(3))
                .subscribe(
                        System.out::println,
                        System.out::println,
                        () -> System.out.println("Completed"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 100
        // 100
        // 100
        // 150
        // 150
        // 150
        // Completed
    }


    //
    // Test
    //

    @Test
    public void testConcatMap() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();
        TestScheduler scheduler = Schedulers.test();

        Observable.just(100, 150)
                .concatMap(i ->
                        Observable.interval(i, TimeUnit.MILLISECONDS, scheduler)
                                .map(v -> i)
                                .take(3)
                )
                .subscribe(tester);

        scheduler.advanceTimeBy(750, TimeUnit.MILLISECONDS);
        tester.assertReceivedOnNext(Arrays.asList(100, 100, 100, 150, 150, 150));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new ConcatMap().exampleConcatMap();
    }

}
