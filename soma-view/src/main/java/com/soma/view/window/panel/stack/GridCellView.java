package com.soma.view.window.panel.stack;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.soma.viewmodel.window.panel.Grid;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import reactor.core.scheduler.Schedulers;

public class GridCellView implements Initializable {

    private Grid.Cell cell;
    private StackPane root;

    public GridCellView(Grid.Cell cell) {
        this.cell = cell;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GridCellView.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cell.boundsChanged.publishOn(Schedulers.fromExecutor(Platform::runLater)).subscribe(bounds -> {
            double x = bounds.getMinX();
            double y = bounds.getMinY();
            double width = bounds.getWidth();
            double height = bounds.getHeight();
            //            System.out.println("cell[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "] " + this);

            root.setLayoutX(x);
            root.setLayoutY(y);
            root.setPrefSize(width, height);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Parent getRoot() {
        return root;
    }
}
