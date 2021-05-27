package com.soma.view.window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.soma.view.window.panel.stack.StackPanelView;
import com.soma.viewmodel.window.Window;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class WindowView implements Initializable {

    private Window window;

    private Parent root;

    @FXML
    private StackPane contentPane;

    public WindowView(Window window) {
        System.out.println("WindowView() " + Thread.currentThread().getName());
        this.window = window;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WindowView.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window.stackValues.flatMap(stack -> Mono.just(stack).subscribeOn(Schedulers.boundedElastic()).map(StackPanelView::new))
                .publishOn(Schedulers.fromExecutor(Platform::runLater)).doOnError(Throwable::printStackTrace).subscribe(stackPanelView -> {
                    contentPane.getChildren().setAll(stackPanelView.getRoot());
                });
    }

    public Parent getRoot() {
        return root;
    }
}