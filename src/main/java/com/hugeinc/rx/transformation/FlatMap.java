package com.hugeinc.rx.transformation;

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
 * Date: 12/14/16
 * Time: 5:42 PM
 */
public class FlatMap {

    public void exampleFlatMap() {
        Observable<Integer> values = Observable.just(2);

        values
                .flatMap(i -> Observable.range(0,i))
                .subscribe(new PrintSubscriber("flatMap"));

        // flatMap: 0
        // flatMap: 1
        // flatMap: Completed
    }

    public void exampleFlatMapMultipleValues() {
        Observable<Integer> values = Observable.range(1,3);

        values
                .flatMap(i -> Observable.range(0,i))
                .subscribe(new PrintSubscriber("flatMap"));

        // flatMap: 0
        // flatMap: 0
        // flatMap: 1
        // flatMap: 0
        // flatMap: 1
        // flatMap: 2
        // flatMap: Completed
    }

    public void exampleFlatMapNewType() {
        Observable<Integer> values = Observable.just(1);

        values
                .flatMap(i ->
                        Observable.just(
                                Character.valueOf((char)(i+64))
                        ))
                .subscribe(new PrintSubscriber("flatMap"));

        // flatMap: A
        // flatMap: Completed
    }

    public void exampleFlatMapFilter() {
        Observable<Integer> values = Observable.range(0,30);

        values
                .flatMap(i -> {
                    if (0 < i && i <= 26)
                        return Observable.just(Character.valueOf((char)(i+64)));
                    else
                        return Observable.empty();
                })
                .subscribe(new PrintSubscriber("flatMap"));

        // flatMap: A
        // flatMap: B
        // flatMap: C
        // ...
        // flatMap: X
        // flatMap: Y
        // flatMap: Z
        // flatMap: Completed
    }

    public void exampleFlatMapAsynchronous() {
        Observable.just(100, 150)
                .flatMap(i ->
                        Observable.interval(i, TimeUnit.MILLISECONDS)
                                .map(v -> i)
                )
                .take(10)
                .subscribe(new PrintSubscriber("flatMap"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // flatMap: 100
        // flatMap: 150
        // flatMap: 100
        // flatMap: 100
        // flatMap: 150
        // flatMap: 100
        // flatMap: 150
        // flatMap: 100
        // flatMap: 100
        // flatMap: 150
        // flatMap: Completed
    }


    //
    // Tests
    //

    @Test
    public void testFlatMap() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.just(2);

        values
                .flatMap(i -> Observable.range(0,i))
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0,1));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testFlatMapMultipleValues() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(1,3);

        values
                .flatMap(i -> Observable.range(0,i))
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0,0,1,0,1,2));
        tester.assertTerminalEvent();
        tester.assertNoErrors();

    }

    @Test
    public void testFlatMapNewType() {
        TestSubscriber<Character> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.just(1);

        values
                .flatMap(i ->
                        Observable.just(
                                Character.valueOf((char)(i+64))
                        ))
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList('A'));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testFlatMapFilter() {
        TestSubscriber<Character> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0,30);

        values
                .flatMap(i -> {
                    if (0 < i && i <= 26)
                        return Observable.just(Character.valueOf((char)(i+64)));
                    else
                        return Observable.empty();
                })
                .subscribe(tester);

        assertEquals(tester.getOnNextEvents().size(), 26);
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testFlatMapAsynchronous() {
        TestSubscriber<Object> tester = new TestSubscriber<>();
        TestScheduler scheduler = Schedulers.test();

        Observable.just(100, 150)
                .flatMap(i ->
                        Observable.interval(i, TimeUnit.MILLISECONDS, scheduler)
                                .map(v -> i)
                )
                .take(10)
                .distinctUntilChanged()
                .subscribe(tester);

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        assertTrue(tester.getOnNextEvents().size() > 2); // 100 and 150 succeeded each other more than once
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new FlatMap().exampleFlatMapAsynchronous();
    }
}
