package com.soma.view;

import com.soma.viewmodel.ViewModel;
import com.soma.viewmodel.window.Window;
import com.soma.viewmodel.window.panel.Content;
import com.soma.viewmodel.window.panel.Grid;
import com.soma.viewmodel.window.panel.Stack;

import javafx.application.Application;
import javafx.stage.Stage;
import reactor.core.scheduler.Schedulers;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Schedulers.single().schedule(() -> {
            Grid grid1 = new Grid("Search", Grid.Type.Search);
            Grid grid2 = new Grid("Preferences", Grid.Type.Preferences);

            Grid.Cell cell1 = new Grid.Cell(0, 0, 1, 0.25, new Stack(new Content("Toolbar", Content.Type.Toolbar)));
            Grid.Cell cell2 = new Grid.Cell(0, 0.25, 0.25, 1, new Stack(new Content("Thumbs", Content.Type.Thumbs)));
            Grid.Cell cell3 = new Grid.Cell(0.25, 0.25, 1, 1, new Stack(new Content("Image2d", Content.Type.Image2d)));

            Grid grid3 = new Grid("Przemysław Krysztofiak", Grid.Type.Study, cell1, cell2, cell3);

//            Window window = new Window(new Stack(grid1, grid2, grid3));
            Window window = new Window(new Stack(grid3));
            Window window2 = new Window(new Stack());
            new View(new ViewModel(window, window2));
        });
    }
}