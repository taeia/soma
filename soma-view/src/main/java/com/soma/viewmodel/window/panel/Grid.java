package com.soma.viewmodel.window.panel;

import com.github.taeia.reactor.fx.Additions;
import com.github.taeia.reactor.fx.Values;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reactor.core.publisher.Flux;
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
    public final Flux<Bounds> boundsChanged = Values.of(boundsProperty, Schedulers.single());
    
    public Grid(String title, Type type, Cell... cells) {
        this.title = title;
        this.type = type;
		this.cells.setAll(cells);

        registerListeners();
	}
	
    private void registerListeners() {
        // TODO change to parallel and remove subscription after cell removed
        cellAdded.flatMap(cell -> Flux.combineLatest(cell.ratioBoundsChanged, boundsChanged, this::calculateCellBounds).doOnNext(cell::setBounds))
                .doOnError(Throwable::printStackTrace).subscribe();
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
        boundsProperty.set(bounds);
    }

    private Bounds calculateCellBounds(Bounds cellRatioBounds, Bounds gridBounds) {
        double minX = cellRatioBounds.getMinX() * gridBounds.getWidth();
        double minY = cellRatioBounds.getMinY() * gridBounds.getHeight();
        double maxX = cellRatioBounds.getMaxX() * gridBounds.getWidth();
        double maxY = cellRatioBounds.getMaxY() * gridBounds.getHeight();
        
        return new Bounds.Builder().minX(minX).minY(minY).maxX(maxX).maxY(maxY).build();
    }

    public static class Cell {

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
    }
}