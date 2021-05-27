package com.soma.view.window.panel.stack;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.soma.viewmodel.window.panel.Panel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class TabView implements Initializable {

    private final Panel panel;

    private Parent root;

    @FXML
    private Label titleLabel;

    public TabView(Panel panel) {
        this.panel = panel;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TabView.fxml"));
        fxmlLoader.setController(this);

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(panel.getTitle());
    }
}
