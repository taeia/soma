package com.soma.view;

import java.util.concurrent.TimeUnit;

import com.github.taeia.reactor.fx.Values;

import javafx.beans.property.SimpleStringProperty;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ReactorAsyncWithBackpressureLatestExample {

    public static void main(String[] args) throws InterruptedException {

        Flux.just(1).subscribeOn(Schedulers.single()).subscribe(next -> {
            System.out.println(Thread.currentThread().getName());
        });

        Flux<String> first = Values.of(new SimpleStringProperty(), Schedulers.single());
        Flux<String> second = Values.of(new SimpleStringProperty(), Schedulers.single());
        Flux.combineLatest(first, second, (a, b) -> 2).subscribe();


        TimeUnit.SECONDS.sleep(1);

//        Flux<Object> fluxAsyncBackp = Flux.create(emitter -> {
//            for (int i = 0; i < 1000; i++) {
//                System.out.println(Thread.currentThread().getName() + " | Publishing = " + i);
//                emitter.next(i);
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            emitter.complete();
//
//        }, OverflowStrategy.LATEST);
//
//        fluxAsyncBackp.subscribeOn(Schedulers.elastic()).doOnNext(i -> {
//            System.out.println(Thread.currentThread().getName() + " | Next = " + i);
//        }).publishOn(Schedulers.elastic()).subscribe(i -> {
//            System.out.println(Thread.currentThread().getName() + " | Received = " + i);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//        }, e -> {
//            // Process error
//            System.err.println(Thread.currentThread().getName() + " | Error = " + e.getMessage());
//        });
//        Thread.sleep(100000);
    }
}