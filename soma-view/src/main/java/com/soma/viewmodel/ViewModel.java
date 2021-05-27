package com.soma.viewmodel;

import com.github.taeia.reactor.fx.Additions;
import com.soma.viewmodel.window.Window;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ViewModel {

    private final ObservableList<Window> windows = FXCollections.observableArrayList();
    public final Flux<Window> windowAdded = Additions.of(windows, Schedulers.single()).startWith(windows);

    public ViewModel(Window... windows) {
        this.windows.setAll(windows);
    }
}