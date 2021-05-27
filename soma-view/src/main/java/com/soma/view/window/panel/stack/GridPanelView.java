package com.soma.view.window.panel.stack;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.taeia.reactor.fx.Values;
import com.soma.viewmodel.window.panel.Bounds;
import com.soma.viewmodel.window.panel.Grid;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class GridPanelView implements Initializable {

    private final Grid grid;
    private Pane root;

    private final ObservableList<GridCellView> cells = FXCollections.observableArrayList();

    public GridPanelView(Grid grid) {
        this.grid = grid;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GridPanelView.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.cellAdded
                .doOnNext(next -> System.out.println("wtf1"))
                .flatMap(cell -> Mono.just(cell).subscribeOn(Schedulers.boundedElastic()).doOnNext(next -> System.out.println("wtf!")).map(GridCellView::new)
                        .doOnNext(cells::add).map(GridCellView::getRoot))
                .publishOn(Schedulers.fromExecutor(Platform::runLater)).doOnNext(root.getChildren()::add).doOnError(Throwable::printStackTrace).subscribe();

        Flux<Double> widthChanged = Values.of(root.widthProperty(), Schedulers.fromExecutor(Platform::runLater)).map(Number::doubleValue);
        Flux<Double> heightChanged = Values.of(root.heightProperty(), Schedulers.fromExecutor(Platform::runLater)).map(Number::doubleValue);

        
        Flux.combineLatest(widthChanged, heightChanged, (width, height) -> new Bounds.Builder().minX(0).minY(0).width(width).height(height).build())
                .publishOn(Schedulers.single()).doOnNext(grid::setBounds).doOnError(Throwable::printStackTrace).subscribe();

//        widthChanged.publishOn(Schedulers.single()).doOnNext(grid::setWidth).doOnError(Throwable::printStackTrace).subscribe();
//        heightChanged.publishOn(Schedulers.single()).doOnNext(grid::setHeight).doOnError(Throwable::printStackTrace).subscribe();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Parent getRoot() {
        return root;
    }
}