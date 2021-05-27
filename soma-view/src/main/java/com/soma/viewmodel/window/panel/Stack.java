package com.soma.viewmodel.window.panel;

import com.github.taeia.reactor.fx.Additions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Stack implements Pane {

	private final ObservableList<Panel> panels = FXCollections.observableArrayList();
    public final Flux<Panel> panelAdded = Additions.of(panels, Schedulers.single()).startWith(panels);
	
	public Stack(Panel... panels) {
		this.panels.setAll(panels);
	}
}