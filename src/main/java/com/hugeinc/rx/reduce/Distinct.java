package com.hugeinc.rx.reduce;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 3:47 PM
 */
public class Distinct {

    public void exampleDistinct() {
        Observable<Integer> values = Observable.create(o -> {
            o.onNext(1);
            o.onNext(1);
            o.onNext(2);
            o.onNext(3);
            o.onNext(2);
            o.onCompleted();
        });

        values
                .distinct()
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 1
        // 2
        // 3
        // Completed
    }

    public void exampleDistinctKey() {
        Observable<String> values = Observable.create(o -> {
            o.onNext("First");
            o.onNext("Second");
            o.onNext("Third");
            o.onNext("Fourth");
            o.onNext("Fifth");
            o.onCompleted();
        });

        values
                .distinct(v -> v.charAt(0))
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // First
        // Second
        // Third
        // Completed
    }

    public void exampleDistinctUntilChanged() {
        Observable<Integer> values = Observable.create(o -> {
            o.onNext(1);
            o.onNext(1);
            o.onNext(2);
            o.onNext(3);
            o.onNext(2);
            o.onCompleted();
        });

        values
                .distinctUntilChanged()
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 1
        // 2
        // 3
        // 2
        // Completed
    }

    public void exampleDistinctUntilChangedKey() {
        Observable<String> values = Observable.create(o -> {
            o.onNext("First");
            o.onNext("Second");
            o.onNext("Third");
            o.onNext("Fourth");
            o.onNext("Fifth");
            o.onCompleted();
        });

        values
                .distinctUntilChanged(v -> v.charAt(0))
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // First
        // Second
        // Third
        // Fourth
        // Completed
    }


    //
    // Tests
    //

    @Test
    public void testDistinct() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.create(o -> {
            o.onNext(1);
            o.onNext(1);
            o.onNext(2);
            o.onNext(3);
            o.onNext(2);
            o.onCompleted();
        });

        values
                .distinct()
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(1,2,3));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testDistinctKey() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.create(o -> {
            o.onNext("First");
            o.onNext("Second");
            o.onNext("Third");
            o.onNext("Fourth");
            o.onNext("Fifth");
            o.onCompleted();
        });

        values
                .distinct(v -> v.charAt(0))
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList("First", "Second", "Third"));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testDistinctUntilChanged() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.create(o -> {
            o.onNext(1);
            o.onNext(1);
            o.onNext(2);
            o.onNext(3);
            o.onNext(2);
            o.onCompleted();
        });

        values
                .distinctUntilChanged()
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(1,2,3,2));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testDistinctUntilChangedKey() {
        TestSubscriber<String> tester = new TestSubscriber<>();

        Observable<String> values = Observable.create(o -> {
            o.onNext("First");
            o.onNext("Second");
            o.onNext("Third");
            o.onNext("Fourth");
            o.onNext("Fifth");
            o.onCompleted();
        });

        values
                .distinctUntilChanged(v -> v.charAt(0))
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList("First", "Second", "Third", "Fourth"));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new Distinct().exampleDistinctUntilChangedKey();
    }
}
