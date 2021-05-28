package com.soma.view;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FlatMapApp {

    public static void main(String[] args) throws InterruptedException {
        ObjectProperty<Integer> numberProperty = new SimpleObjectProperty<>();
        Flux<Integer> flux = Flux.create(sink -> {
            numberProperty.addListener(new ChangeListener<Integer>() {

                @Override
                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue,
                        Integer newValue) {
                    sink.next(newValue);
                }
            });
        });

        // @formatter:off
//        Flux.range(0, 10)
//        Flux.<Integer>create(sink -> {
//            numberProperty.addListener(new ChangeListener<Integer>() {
//
//                @Override
//                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue,
//                        Integer newValue) {
//                    sink.next(newValue);
//                }
//            });
//        })
        flux
        .onBackpressureLatest()
        .flatMap(i -> Mono.just(i)
                .subscribeOn(Schedulers.parallel())
//                .log()
                .map(FlatMapApp::process), 4)
//        .log()
        .subscribe();
        // @formatter:on

        //        Flux.range(1, 100).log().map(next -> next + 1).log().map(next -> next + 1).log().subscribe();

        Schedulers.single().schedule(() -> {
            for (int i = 0; i < 100; i++) {
                numberProperty.set(i);
            }
        });

        TimeUnit.SECONDS.sleep(100);
    }

    public static int process(int i) {
        System.out.println("start i=" + i + ", " + Thread.currentThread().getName());
        Random random = new Random();
        int sleep = random.nextInt(1000);
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("  end i=" + i + ", " + Thread.currentThread().getName());
        return i;
    }
}