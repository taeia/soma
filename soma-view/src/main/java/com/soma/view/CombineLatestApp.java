package com.soma.view;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.taeia.reactor.fx.Values;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class CombineLatestApp {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        //        One<String> sink = Sinks.one();
        //
        //        Many<String> sink1 = Sinks.many().replay().latest();
        //        Many<String> sink2 = Sinks.many().replay().latest();

        ObjectProperty<String> stringProperty1 = new SimpleObjectProperty<>();
        Flux<String> stringFlux1 = Values.of(stringProperty1, Schedulers.single());

        ObjectProperty<String> stringProperty2 = new SimpleObjectProperty<>();
        Flux<String> stringFlux2 = Values.of(stringProperty2, Schedulers.single());


        Flux.combineLatest(stringFlux1, stringFlux2, Pair<String, String>::new)
        .onBackpressureLatest()
                .flatMapSequential(pair -> Mono.just(pair).subscribeOn(Schedulers.parallel()).map(
                        __ -> process(pair.getKey(), pair.getValue())), 4).subscribe(result -> {
                            System.out.println("result=" + result);
                        });
       
        
        
        
        //        Flux.combineLatest(stringFlux1, stringFlux2, Pair<String, String>::new).flatMap(
        //                arg -> Mono.just(arg).subscribeOn(Schedulers.parallel()).map(
        //                        args2 -> process(args2.getKey(), args2.getValue()))).subscribe(result -> {
        //                            System.out.println("result=" + result);
        //                        });
        
//        Flux.combineLatest(stringFlux1.flatMap(next -> Mono.just(next).subscribeOn(Schedulers.parallel())),
//                stringFlux2.flatMap(next -> Mono.just(next).subscribeOn(Schedulers.parallel())),
//                CombineLatestApp::process).subscribe(result -> {
//                    System.out.println("result=" + result);
//                });
        

        TimeUnit.SECONDS.sleep(2);

        Schedulers.newSingle("1").schedule(() -> {
            for (int i = 0; i < 100; i++) {
                //                Random random = new Random();
                //                int sleep = random.nextInt(100);
                //                try {
                //                    TimeUnit.MILLISECONDS.sleep(sleep);
                //                } catch (InterruptedException e) {
                //                    e.printStackTrace();
                //                }
                stringProperty1.set(String.valueOf(i));
            }
        });

        Schedulers.newSingle("2").schedule(() -> {
            for (int i = 100; i < 200; i++) {
                //                Random random = new Random();
                //                int sleep = random.nextInt(101);
                //                try {
                //                    TimeUnit.MILLISECONDS.sleep(sleep);
                //                } catch (InterruptedException e) {
                //                    e.printStackTrace();
                //                }
                stringProperty2.set(String.valueOf(i));
            }
        });

        //        });
        TimeUnit.SECONDS.sleep(100);
    }

    private static String process(String s1, String s2) {
        Random random = new Random();
        int sleep = random.nextInt(1000);
        System.out.println(
                "start " + s1 + "-" + s2 + ", " + Thread.currentThread().getName() + ", sleep=" + sleep);
        String result = s1 + "-" + s2;
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(
                "  end " + s1 + "-" + s2 + ", " + Thread.currentThread().getName() + ", sleep=" + sleep);
        System.out.println(counter.incrementAndGet());
        return result;
    }
}
