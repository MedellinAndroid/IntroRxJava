package com.hugeinc.rx.transformation;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 5:16 PM
 */
public class Map {

    public void exampleMap() {
        Observable<Integer> values = Observable.range(0, 4);

        values
                .map(i -> i + 3)
                .subscribe(new PrintSubscriber("Map"));

        // Map: 3
        // Map: 4
        // Map: 5
        // Map: 6
        // Map: Completed
    }

    public void exampleMap2() {
        Observable<Integer> values =
                Observable.just("0", "1", "2", "3")
                        .map(Integer::parseInt);

        values.subscribe(new PrintSubscriber("Map"));

        // Map: 0
        // Map: 1
        // Map: 2
        // Map: 3
        // Map: Completed
    }


    //
    // Tests
    //

    @Test
    public void testMap() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0, 4);

        values
                .map(i -> i + 3)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(3, 4, 5, 6));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testMap2() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values =
                Observable.just("0", "1", "2", "3")
                        .map(Integer::parseInt);

        values.subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0, 1, 2, 3));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new Map().exampleMap2();
    }
}
