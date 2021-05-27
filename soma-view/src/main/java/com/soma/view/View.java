package com.soma.view;

import com.soma.view.window.WindowView;
import com.soma.viewmodel.ViewModel;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class View {

    public View(ViewModel viewModel) {

        viewModel.windowAdded.flatMap(window -> Mono.just(window).subscribeOn(Schedulers.boundedElastic()).map(WindowView::new))
                .publishOn(Schedulers.fromExecutor(Platform::runLater)).doOnError(Throwable::printStackTrace).subscribe(windowView -> {
                    Stage stage = new Stage();
                    Scene scene = new Scene(windowView.getRoot(), 400, 400);
                    scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
                });
    }
}