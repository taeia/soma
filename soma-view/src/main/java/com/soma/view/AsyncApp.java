package com.soma.view;

import java.util.concurrent.TimeUnit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class AsyncApp {

    public static void main(String[] args) throws InterruptedException {

        ObjectProperty<String> numberProperty1 = new SimpleObjectProperty<>("one");
        Flux<String> numberChanged1 = Flux.create(sink -> {
            numberProperty1.addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                        String newValue) {
                    sink.next(newValue);
                }
            });
        });

        ObjectProperty<String> numberProperty2 = new SimpleObjectProperty<>("two");
        Flux<String> numberChanged2 = Flux.create(sink -> {
            numberProperty2.addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                        String newValue) {
                    sink.next(newValue);
                }
            });
        });

        // @formatter:off
        numberChanged1.flatMap(number -> 
            Mono.just(number)
            .zipWith(Mono.just(numberProperty2.get()), AsyncApp::process)
            .subscribeOn(Schedulers.parallel()))
        .subscribe(next -> {
            System.out.println("result=" + next);
        });
        // @formatter:on

        TimeUnit.SECONDS.sleep(1);

        numberProperty1.set("three");

        //        numberProperty1.set("three");

        // @formatter:off
//        Mono.just("1")
//        .zipWith(Mono.just("2"), AsyncApp::process)
//        .subscribeOn(Schedulers.parallel())
//        .subscribe(next -> {
//            System.out.println("next=" + next);
//        });
        // @formatter:on

        //        Mono.zip(Mono.just(""), Mono.just(1)).map(tuple2 -> tuple2.get)
        //        Mono.zip("one", 1)
        
//        Mono.zip((s1, s2, s3) -> {
//            return null;
//        }, Mono.just(""), Mono.just(1), Mono.just(4));
        
        TimeUnit.SECONDS.sleep(10);
    }

    private static String process(String s1, String s2) {
        System.out.println("Thread=" + Thread.currentThread().getName());
        return s1 + "-" + s2;
    }
}