package com.hugeinc.rx.transformation;

import org.junit.jupiter.api.Test;
import rx.Notification;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

/**
 * User: HUGE-gilbert
 * Date: 12/14/16
 * Time: 5:38 PM
 */
public class Materialize {

    public void exampleMaterialize() {
        Observable<Integer> values = Observable.range(0,3);

        values.take(3)
                .materialize()
                .subscribe(new PrintSubscriber("Materialize"));

        // Materialize: [rx.Notification@a4c802e9 OnNext 0]
        // Materialize: [rx.Notification@a4c802ea OnNext 1]
        // Materialize: [rx.Notification@a4c802eb OnNext 2]
        // Materialize: [rx.Notification@18d48ace OnCompleted]
        // Materialize: Completed
    }


    //
    // Tests
    //

    @Test
    public void testMaterialize() {
        TestSubscriber<Notification<Integer>> tester = new TestSubscriber<>();

        Observable<Integer> values = Observable.range(0,3);

        values.take(3)
                .materialize()
                .subscribe(tester);

        tester.assertReceivedOnNext(Arrays.asList(
                Notification.createOnNext(0),
                Notification.createOnNext(1),
                Notification.createOnNext(2),
                Notification.createOnCompleted()
        ));
        tester.assertTerminalEvent();
        tester.assertNoErrors();
    }

    public static void main(String[] args) {
        new Materialize().exampleMaterialize();
    }
}
