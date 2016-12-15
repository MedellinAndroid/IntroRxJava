package com.hugeinc.rx.reduce;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 3:52 PM
 */
public class Ignore {

    public void exampleIgnoreElements() {
        Observable<Integer> values = Observable.range(0, 10);

        values
                .ignoreElements()
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // Completed
    }


    //
    // Tests
    //

    @Test
    public void testIgnoreElements() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0, 10);

        values
                .ignoreElements()
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList());
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new Ignore().exampleIgnoreElements();
    }
}
