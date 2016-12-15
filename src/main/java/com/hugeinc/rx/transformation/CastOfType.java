package com.hugeinc.rx.transformation;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 5:21 PM
 */
public class CastOfType {

    public void exampleCast() {
        Observable<Object> values = Observable.just(0, 1, 2, 3);

        values
                .cast(Integer.class)
                .subscribe(new PrintSubscriber("Cast"));

        // Map: 0
        // Map: 1
        // Map: 2
        // Map: 3
        // Map: Completed
    }

    public void exampleCastFail() {
        Observable<Object> values = Observable.just(0, 1, 2, "3");

        values
                .cast(Integer.class)
                .subscribe(new PrintSubscriber("Cast"));

        // Map: 0
        // Map: 1
        // Map: 2
        // Map: Error: java.lang.ClassCastException: Cannot cast java.lang.String to java.lang.Integer
    }

    public void exampleTypeOf() {
        Observable<Object> values = Observable.just(0, 1, "2", 3);

        values
                .ofType(Integer.class)
                .subscribe(new PrintSubscriber("OfType"));

        // Map: 0
        // Map: 1
        // Map: 3
        // Map: Completed
    }


    //
    // Tests
    //

    @Test
    public void testCast() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Object> values = Observable.just(0, 1, 2, 3);

        values
                .cast(Integer.class)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0, 1, 2, 3));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testCastFail() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Object> values = Observable.just(0, 1, 2, "3");

        values
                .cast(Integer.class)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0, 1, 2));
        tester.assertTerminalEvent();
        assertEquals(tester.getOnErrorEvents().size(), 1); // received 1 error
    }

    @Test
    public void testTypeOf() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Object> values = Observable.just(0, 1, "2", 3);

        values
                .ofType(Integer.class)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0, 1, 3));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new CastOfType().exampleTypeOf();
    }
}
