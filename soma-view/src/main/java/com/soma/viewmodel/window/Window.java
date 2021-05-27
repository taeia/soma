package com.soma.viewmodel.window;

import com.github.taeia.reactor.fx.Values;
import com.soma.viewmodel.window.panel.Stack;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Window {

	private final Property<Stack> stackProperty = new SimpleObjectProperty<>();
    public final Flux<Stack> stackValues = Values.of(stackProperty, Schedulers.single());

	public Window(Stack stack) {
		stackProperty.setValue(stack);
	}
}