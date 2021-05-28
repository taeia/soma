package com.soma.view;

import java.util.concurrent.TimeUnit;

public class BackpressureApp2 {

    public static void main(String[] args) throws InterruptedException {

        //        Flux<Width> flux = Flux.create(sink -> {
        //            stage.widthProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
        //                Width width = new Width(id++, newValue.doubleValue());
        //                System.out.println("[" + Thread.currentThread().getName() + "] PUBLISH width=" + width + ", time=" + System.currentTimeMillis());
        //                sink.next(width);
        //            });
        //        }, OverflowStrategy.LATEST);
        //        flux.limitRate(4).concatMap(width -> Mono.just(width).subscribeOn(computation).map(this::process)).publishOn(Schedulers.single()).subscribe(width -> {
        //            System.out.println("[" + Thread.currentThread().getName() + "] RECEIVED width=" + width + ", time=" + System.currentTimeMillis());
        //        });

        //        Scheduler parallel = Schedulers.newParallel("my-parallel");
        //
        //        ObjectProperty<Integer> numberProperty = new SimpleObjectProperty<>();
        //        Flux.<Integer> create(sink -> {
        //            numberProperty.addListener(new ChangeListener<Integer>() {
        //
        //                @Override
        //                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue,
        //                        Integer newValue) {
        //                    sink.next(newValue);
        //                }
        //            });
        //        }).flatMap(
        //                next -> Mono.just(next).subscribeOn(parallel)).map(BackpressureApp2::process).publishOn(
        //                        Schedulers.single()).subscribe(
        //                        result -> {
        //                    System.out.println("result=" + result);
        //                });
        //
        //        Schedulers.single().schedule(() -> {
        //            for (int i = 0; i < 100; i++) {
        //                numberProperty.setValue(i);
        //            }
        //        });
        //
        //        TimeUnit.SECONDS.sleep(200);
    }

    private static int process(int i) {
        System.out.println("start i=" + i + ", Thread=" + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("  end i=" + i + ", Thread=" + Thread.currentThread().getName());
        return i + 1;
    }
}