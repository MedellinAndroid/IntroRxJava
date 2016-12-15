package com.hugeinc.rx.reduce;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 3:55 PM
 */
public class TakeSkip {

    public void exampleTake() {
        Observable<Integer> values = Observable.range(0, 5);

        values
                .take(2)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 0
        // 1
        // Completed
    }

    public void exampleSkip() {
        Observable<Integer> values = Observable.range(0, 5);

        values
                .skip(2)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 2
        // 3
        // 4
        // Completed
    }

    public void exampleTakeTime() {
        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

        Subscription subscription = values
                .take(250, TimeUnit.MILLISECONDS)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscription.unsubscribe();

        // 0
        // 1
        // Completed
    }

    public void exampleSkipTime() {
        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

        Subscription subscription = values
                .skip(250, TimeUnit.MILLISECONDS)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscription.unsubscribe();

        // 2
        // 3
        // 4
        // ...
    }

    public void exampleTakeWhile() {
        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

        Subscription subscription = values
                .takeWhile(v -> v < 2)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscription.unsubscribe();

        // 0
        // 1
        // Completed
    }

    public void exampleSkipWhile() {
        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

        Subscription subscription = values
                .skipWhile(v -> v < 2)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subscription.unsubscribe();

        // 2
        // 3
        // 4
        // ...
    }

    public void exampleSkipLast() {
        Observable<Integer> values = Observable.range(0,5);

        values
                .skipLast(2)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 0
        // 1
        // 2
        // Completed
    }

    public void exampleTakeLast() {
        Observable<Integer> values = Observable.range(0,5);

        values
                .takeLast(2)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        // 3
        // 4
        // Completed
    }

    public void exampleTakeUntil() {
        Observable<Long> values = Observable.interval(100,TimeUnit.MILLISECONDS);
        Observable<Long> cutoff = Observable.timer(250, TimeUnit.MILLISECONDS);

        Subscription subscription = values
                .takeUntil(cutoff)
                .subscribe(
                        new Action1<Long>() {
                            @Override
                            public void call(Long v) {
                                System.out.println(v);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                System.out.println("Error: " + throwable);
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                System.out.println("Complete");
                            }
                        }
                );

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        subscription.unsubscribe();

        // 0
        // 1
        // Completed
    }

    public void exampleSkipUntil() {
        Observable<Long> values = Observable.interval(100,TimeUnit.MILLISECONDS);
        Observable<Long> cutoff = Observable.timer(250, TimeUnit.MILLISECONDS);

        Subscription subscription = values
                .skipUntil(cutoff)
                .subscribe(
                        v -> System.out.println(v),
                        e -> System.out.println("Error: " + e),
                        () -> System.out.println("Completed")
                );

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        subscription.unsubscribe();

        // 2
        // 3
        // 4
        // ...
    }


    //
    // Tests
    //

    @Test
    public void testTake() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0, 5);

        values
                .take(2)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0,1));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testSkip() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0, 5);

        values
                .skip(2)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(2,3,4));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testTakeTime() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester = new TestSubscriber<>();

        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS ,scheduler);

        Subscription subscription = values
                .take(250, TimeUnit.MILLISECONDS, scheduler)
                .subscribe(tester);

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();

        tester.assertReceivedOnNext(Arrays.asList(0L,1L));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testSkipTime() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester = new TestSubscriber<>();

        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler);

        Subscription subscription = values
                .skip(250, TimeUnit.MILLISECONDS, scheduler)
                .subscribe(tester);

        scheduler.advanceTimeBy(550, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();

        tester.assertReceivedOnNext(Arrays.asList(2L, 3L, 4L));
        tester.assertNoErrors();
    }

    @Test
    public void testTakeWhile() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester = new TestSubscriber<>();

        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler);

        Subscription subscription = values
                .takeWhile(v -> v < 2)
                .subscribe(tester);

        scheduler.advanceTimeBy(550, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();

        tester.assertReceivedOnNext(Arrays.asList(0L, 1L));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testSkipWhile() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester = new TestSubscriber<>();

        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler);

        Subscription subscription = values
                .skipWhile(v -> v < 2)
                .subscribe(tester);

        scheduler.advanceTimeBy(550, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();

        tester.assertReceivedOnNext(Arrays.asList(2L, 3L, 4L));
        tester.assertNoErrors();
    }

    @Test
    public void testerSkipLast() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0,5);

        values
                .skipLast(2)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(0,1,2));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testTakeLast() {
        TestSubscriber<Integer> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0,5);

        values
                .takeLast(2)
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(3,4));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testTakeUntil() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester = new TestSubscriber<>();

        Observable<Long> values = Observable.interval(100,TimeUnit.MILLISECONDS, scheduler);
        Observable<Long> cutoff = Observable.timer(250, TimeUnit.MILLISECONDS, scheduler);

        Subscription subscription = values
                .takeUntil(cutoff)
                .subscribe(tester);

        scheduler.advanceTimeBy(550, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();

        tester.assertReceivedOnNext(Arrays.asList(0L,1L));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testSkipUntil() {
        TestScheduler scheduler = Schedulers.test();
        TestSubscriber<Long> tester = new TestSubscriber<>();

        Observable<Long> values = Observable.interval(100,TimeUnit.MILLISECONDS, scheduler);
        Observable<Long> cutoff = Observable.timer(250, TimeUnit.MILLISECONDS, scheduler);

        Subscription subscription = values
                .skipUntil(cutoff)
                .subscribe(tester);

        scheduler.advanceTimeBy(550, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();

        tester.assertReceivedOnNext(Arrays.asList(2L,3L,4L));
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new TakeSkip().exampleTakeTime();
    }

}
