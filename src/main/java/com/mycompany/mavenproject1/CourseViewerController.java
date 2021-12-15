package com.mycompany.mavenproject1;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

public class CourseViewerController {

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }
    
    private void buildUI() throws IOException {
        TilePane tilePane = new TilePane();
    }
}
