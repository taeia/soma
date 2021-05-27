package com.soma.view.window.panel.stack;

import java.net.URL;
import java.util.ResourceBundle;

import com.soma.viewmodel.window.panel.Stack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TabsView implements Initializable {

    private final Stack stack;

    @FXML
    private HBox tabsPane;

    public TabsView(Stack stack) {
        this.stack = stack;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stack.panelAdded.flatMap(stack -> Mono.just(stack).subscribeOn(Schedulers.boundedElastic()).map(TabView::new))
                .publishOn(Schedulers.fromExecutor(Platform::runLater)).subscribe(tabView -> {
                    tabsPane.getChildren().add(tabView.getRoot());
                });
    }
}