package com.hugeinc.rx.transformation;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 5:31 PM
 */
public class TimestampTimeinterval {


    public void exampleTimestamp() {
        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

        values.take(3)
                .timestamp()
                .subscribe(new PrintSubscriber("Timestamp"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Timestamp: Timestamped(timestampMillis = ??, value = 0)
        // Timestamp: Timestamped(timestampMillis = ??, value = 1)
        // Timestamp: Timestamped(timestampMillis = ??, value = 2)
        // Timestamp: Completed
    }

    public void exampleTimeInteval() {
        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

        values.take(3)
                .timeInterval()
                .subscribe(new PrintSubscriber("TimeInterval"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // TimeInterval: TimeInterval [intervalInMilliseconds=???, value=0]
        // TimeInterval: TimeInterval [intervalInMilliseconds=??, value=1]
        // TimeInterval: TimeInterval [intervalInMilliseconds=??, value=2]
        // TimeInterval: Completed
    }


    //
    // Tests
    //

    @Test
    public void testTimestamp() {
        TestSubscriber<Timestamped<Long>> tester = new TestSubscriber<>();
        TestScheduler scheduler = Schedulers.test();

        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler);

        values.take(3)
                .timestamp(scheduler)
                .subscribe(tester);

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        assertEquals(tester.getOnNextEvents().get(0).getTimestampMillis(), 100);
        assertEquals(tester.getOnNextEvents().get(1).getTimestampMillis(), 200);
        assertEquals(tester.getOnNextEvents().get(2).getTimestampMillis(), 300);
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    @Test
    public void testTimeInteval() {
        TestSubscriber<TimeInterval<Long>> tester = new TestSubscriber<>();
        TestScheduler scheduler = Schedulers.test();

        Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler);

        values.take(3)
                .timeInterval(scheduler)
                .subscribe(tester);

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        assertEquals(tester.getOnNextEvents().get(0).getIntervalInMilliseconds(), 100);
        assertEquals(tester.getOnNextEvents().get(1).getIntervalInMilliseconds(), 100);
        assertEquals(tester.getOnNextEvents().get(2).getIntervalInMilliseconds(), 100);
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new TimestampTimeinterval().exampleTimeInteval();
    }
}
