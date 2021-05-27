package com.soma.view;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class BackpressureApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private int id = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Scheduler computation = Schedulers.newParallel("Computation", 4);


        Flux<Width> flux = Flux.create(sink -> {
            stage.widthProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
                Width width = new Width(id++, newValue.doubleValue());
                System.out.println("[" + Thread.currentThread().getName() + "] PUBLISH width=" + width + ", time=" + System.currentTimeMillis());
                sink.next(width);
            });
        }, OverflowStrategy.LATEST);
        flux.limitRate(4).concatMap(width -> Mono.just(width).subscribeOn(computation).map(this::process)).publishOn(Schedulers.single()).subscribe(width -> {
            System.out.println("[" + Thread.currentThread().getName() + "] RECEIVED width=" + width + ", time=" + System.currentTimeMillis());
        });

        stage.setScene(new Scene(new StackPane()));
        stage.show();
    }

    public Width process(Width width) {
        Random random = new Random();
        int next = random.nextInt(1000);
        System.out
                .println("[" + Thread.currentThread().getName() + "] START PROCESS width=" + width + " sleep=" + next + ", time=" + System.currentTimeMillis());
        try {
            TimeUnit.MILLISECONDS.sleep(next);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(
                "[" + Thread.currentThread().getName() + "] FINISHED PROCESS width=" + width + " sleep=" + next + ", time=" + System.currentTimeMillis());
        return width;
    }

}

class Width {

    private final int id;
    private final double width;

    public Width(int id, double width) {
        super();
        this.id = id;
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "Width[id=" + id + ", width=" + width + "]";
    }
}