package com.soma.viewmodel.window.panel;

import java.util.function.Function;

import org.reactivestreams.Publisher;

import com.github.taeia.reactor.fx.Additions;
import com.github.taeia.reactor.fx.Values;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Grid implements Pane, Panel {

    private final String title;
    private final Type type;
	private final ObservableList<Cell> cells = FXCollections.observableArrayList();
    public final Flux<Cell> cellAdded = Additions.of(cells, Schedulers.single()).startWith(cells);

    private final DoubleProperty widthProperty = new SimpleDoubleProperty();
    private final Flux<Double> widthChanged = Values.of(widthProperty, Schedulers.single()).map(Number::doubleValue);

    private final DoubleProperty heightProperty = new SimpleDoubleProperty();
    private final Flux<Double> heightChanged = Values.of(heightProperty, Schedulers.single()).map(Number::doubleValue);
    
    private final ObjectProperty<Bounds> boundsProperty = new SimpleObjectProperty<>();
    public final Flux<Bounds> boundsChanged = Values.of(boundsProperty);
    //    public final Flux<Bounds> boundsChanged = Values.of(boundsProperty,
    //            Schedulers.single()).onBackpressureLatest().limitRate(1);
    
    public Grid(String title, Type type, Cell... cells) {
        this.title = title;
        this.type = type;
		this.cells.setAll(cells);

        registerListeners();
	}
	
    private void registerListeners() {
        // TODO change to parallel and remove subscription after cell removed
        //        cellAdded.flatMap(cell -> boundsChanged.parallel().runOn(Schedulers.parallel())).publishOn(S)

        cellAdded.doOnNext(this::onCellAdded).subscribe();

        // @formatter:off
//        cellAdded.flatMap(cell -> Flux.combineLatest(cell.ratioBoundsChanged, boundsChanged, Pair<Bounds, Bounds>::new)
//                .onBackpressureLatest()
//                .flatMapSequential(args -> Mono.just(args)
//                        .subscribeOn(Schedulers.parallel())
//                        .doOnNext(next -> System.out.println("cell=" + cell.name + " " + args.getValue()))
//                        .map(arg -> calculateCellBounds(arg.getKey(), arg.getValue())), 4)
//                .publishOn(Schedulers.single())
//                .doOnNext(cellBounds -> {
//                    System.out.println(cell.name + "!!! " + cellBounds);
//                    cell.setBounds(cellBounds);
//                }))
//        .subscribe();
        // @formatter:on
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Panel.Type getPanelType() {
        return Panel.Type.Grid;
    }

    public Grid.Type getGridType() {
        return type;
    }

    public static enum Type {
        Search, Preferences, Study;
    }

    public void setWidth(double width) {
        widthProperty.set(width);
    }

    public void setHeight(double height) {
        heightProperty.set(height);
    }

    public void setBounds(Bounds bounds) {
        Schedulers.single().schedule(() ->  {
            boundsProperty.set(bounds);
        });
    }

    private void onCellAdded(Grid.Cell cell) {

        cell.ratioBoundsChanged.subscribe(next -> System.out.println("kurwa!"));
        //        cell.ratioBoundsChanged.last().subscribe(next -> System.out.println("kurwa2!"));

        // @formatter:off
        boundsChanged
        .onBackpressureLatest()
        .flatMapSequential(bounds ->
            Mono.zip(Mono.just(bounds), Mono.just(cell.getRatioBounds()))
            .subscribeOn(Schedulers.parallel())
            .map(tuple -> calculateCellBounds(tuple.getT2(), tuple.getT1()))
        
//            Mono.just(cell.ratioBoundsChanged.blockLast()).zipWith(Mono.just(bounds), this::calculateCellBounds).subscribeOn(Schedulers.parallel())
        
//            cell.ratioBoundsChanged.last().doOnNext(next -> System.out.println("1")).zipWith(Mono.just(bounds), this::calculateCellBounds).subscribeOn(Schedulers.parallel())
//            Mono.just(cell.ratioBoundsChanged.last()).zipWith(Mono.just(bounds), this::calculateCellBounds)
        
//            Mono.just(bounds).zipWith(cell.ratioBoundsChanged.doOnNext(n -> System.out.println("2")).last(), this::calculateCellBounds).subscribeOn(Schedulers.parallel())
//            Mono.just(bounds).zipWith(Mono.just(ratioBoundsChanged.latest))
        
//            cell.ratioBoundsChanged.take(1).withLatestFrom(Flux.just(bounds).subscribeOn(Schedulers.parallel()), this::calculateCellBounds)
            , 4)
        .publishOn(Schedulers.single())
        .doOnNext(cell::setBounds)
        .subscribe();
        // @formatter:on

        //        Flux.combineLatest(cell.ratioBoundsChanged, boundsChanged, Pair<Bounds, Bounds>::new)
        //        .onBackpressureLatest()
        //        .flatMapSequential(next -> Mono.just(next).subscribeOn(Schedulers.parallel()))
    }

    private Function<Bounds, Publisher<Bounds>> calculateBoundsAsync(
            Grid.Cell cell) {
        return bounds -> cell.ratioBoundsChanged.take(1).withLatestFrom(
                Flux.just(bounds).subscribeOn(Schedulers.parallel()), this::calculateCellBounds);
    }

    private Bounds calculateCellBounds(Bounds cellRatioBounds, Bounds gridBounds) {
        System.out.println("Calculate bounds!!! " + Thread.currentThread().getName());

        double minX = cellRatioBounds.getMinX() * gridBounds.getWidth();
        double minY = cellRatioBounds.getMinY() * gridBounds.getHeight();
        double maxX = cellRatioBounds.getMaxX() * gridBounds.getWidth();
        double maxY = cellRatioBounds.getMaxY() * gridBounds.getHeight();
        Bounds bounds = new Bounds.Builder().minX(minX).minY(minY).maxX(maxX).maxY(maxY).build();
//        System.out.println("start " + bounds + ", " + Thread.currentThread().getName());
        
//        try {
//            Random random = new Random();
//            int sleep = random.nextInt(1000);
//            TimeUnit.MILLISECONDS.sleep(sleep);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        
        
//        System.out.println("  end " + bounds + ", " + Thread.currentThread().getName());
        return bounds;
    }

    public static class Cell {
        
        private static int counter = 0;
        public final String name = "name" + ++counter;

        private final ObjectProperty<Bounds> ratioBoundsProperty = new SimpleObjectProperty<>();
        public final Flux<Bounds> ratioBoundsChanged = Values.of(ratioBoundsProperty, Schedulers.single());

        private final ObjectProperty<Bounds> boundsProperty = new SimpleObjectProperty<>();
        public final Flux<Bounds> boundsChanged = Values.of(boundsProperty, Schedulers.single());

        private final Stack stack;

        public Cell(double ratioMinX, double ratioMinY, double ratioMaxX, double ratioMaxY, Stack stack) {
            this.stack = stack;
            ratioBoundsProperty.set(new Bounds.Builder().minX(ratioMinX).minY(ratioMinY).maxX(ratioMaxX).maxY(ratioMaxY).build());
        }

        public void setBounds(Bounds bounds) {
            boundsProperty.set(bounds);
        }

        public Stack getStack() {
            return stack;
        }

        public Bounds getRatioBounds() {
            return ratioBoundsProperty.get();
        }
    }
}