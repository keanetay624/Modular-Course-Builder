package com.mycompany.mavenproject1;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;

public class HomeController {
    
    @FXML
    Label outputLabel;
    
    @FXML 
    Button navBtnHome, navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, nvBtnOutcomes, navBtnSignOut;
    
    @FXML
    private void userDidHoverCourses() throws IOException {
        outputLabel.setVisible(true);
        outputLabel.setText("Info about Courses");
    }
    
    @FXML
    private void userDidHoverModules() throws IOException {
        outputLabel.setVisible(true);
        outputLabel.setText("Info about Modules");
    }
    
    @FXML
    private void userDidHoverSections() throws IOException {
        outputLabel.setVisible(true);
        outputLabel.setText("Info about Sections");
    }
    
    @FXML
    private void userDidHoverResources() throws IOException {
        outputLabel.setVisible(true);
        outputLabel.setText("Info about Resources");
    }
    
    @FXML
    private void cancelHover() throws IOException {
        outputLabel.setVisible(false);
    }
    
    /* 
    * Navigation Functions
    */

    @FXML 
    private void switchToHome() throws IOException {
        App.setRoot("home");
    }
    
    @FXML
    private void switchToCourseViewer() throws IOException {
        App.setRoot("courseViewer");
    }
    
    @FXML
    private void switchToModuleViewer() throws IOException {
        App.setRoot("moduleViewer");
    }
    
    @FXML 
    private void switchToSectionViewer() throws IOException {
        App.setRoot("sectionViewer");
    }
    
    @FXML 
    private void switchToResourceViewer() throws IOException {
        App.setRoot("resourceViewer");
    }
    
    @FXML
    private void switchToOutcomeViewer() throws IOException {
        App.setRoot("OutcomeViewer");
    }
    
    @FXML
    private void userDidClickSignOut() throws IOException {
        App.setRoot("login");
    }
}
