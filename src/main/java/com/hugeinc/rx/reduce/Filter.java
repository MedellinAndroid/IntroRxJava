package com.hugeinc.rx.reduce;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 3:44 PM
 */
public class Filter {

    public void example() {
        Observable<Integer> values = Observable.range(0, 10);
        values
                .filter(v -> v % 2 == 0)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 0
        // 2
        // 4
        // 6
        // 8
        // Completed
    }


    //
    // Test
    //

    @Test
    public void test() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0, 10);
        values
                .filter(v -> v % 2 == 0)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0, 2, 4, 6, 8));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new Filter().example();
    }
}
