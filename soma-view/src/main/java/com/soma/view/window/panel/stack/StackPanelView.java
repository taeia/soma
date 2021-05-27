package com.soma.view.window.panel.stack;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.soma.viewmodel.window.panel.Grid;
import com.soma.viewmodel.window.panel.Panel;
import com.soma.viewmodel.window.panel.Stack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class StackPanelView implements Initializable {

    private Parent root;
    private Stack stack;

    @FXML
    private StackPane contentPane;

    public StackPanelView(Stack stack) {
        this.stack = stack;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StackPanelView.fxml"));
        fxmlLoader.setControllerFactory(clazz -> {
            if (clazz.equals(StackPanelView.class)) {
                return this;
            } else if (clazz.equals(TabsView.class)) {
                return new TabsView(stack);
            }
            throw new IllegalArgumentException();
        });
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stack.panelAdded.flatMap(panel -> Mono.just(panel).subscribeOn(Schedulers.boundedElastic()).map(this::toView)).doOnNext(contentPane.getChildren()::add)
                .publishOn(Schedulers.fromExecutor(Platform::runLater)).subscribe();
    }

    private Parent toView(Panel panel) {
        return switch (panel.getPanelType()) {
        case Grid: {
            yield new GridPanelView((Grid) panel).getRoot();
        }
        case Content: {
            yield new Pane();
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + panel.getPanelType());
        };
    }

    public Parent getRoot() {
        return root;
    }
}