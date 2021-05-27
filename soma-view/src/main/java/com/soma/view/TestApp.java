package com.soma.view;

import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TestApp {

    public static void main(String[] args) throws InterruptedException {

        Flux.<Integer>create(sink -> {
            for (int i = 0; i < 100; i++) {
                sink.next(i);
            }

        }).flatMapSequential(i -> Mono.just(i).subscribeOn(Schedulers.parallel()).map(TestApp::process)).publishOn(Schedulers.single()).subscribe(i -> {
            System.out.println("RESULT\t\ti=" + i + "\t" + Thread.currentThread().getName());
        });
        TimeUnit.SECONDS.sleep(2000);

    }

    private static int process(int i) {
        i++;
        System.out.println("START\t\ti=" + i + "\t" + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("FINISHED\ti=" + i + "\t" + Thread.currentThread().getName());
        return i;
    }
}
