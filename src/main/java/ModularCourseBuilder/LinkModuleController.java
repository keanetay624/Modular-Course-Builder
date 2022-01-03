/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Keane Local
 */
public class LinkModuleController {
    
    @FXML
    Button btnCancelLinkModule;
    
    @FXML
    Button btnSaveLinkModule;
    
    @FXML
    ComboBox inputModule;
    
    @FXML
    Label lblCourseName;
    
    private static Course selectedCourse;
    
    public void initialize() throws SQLException {
        // initialize the combobox with list of available modules
        System.out.println("Test initialize method");
        ObservableList<Module> modulesList = DatabaseHelper.getModules();
        ObservableList<String> sModulesList = FXCollections.observableArrayList();
        
        for (Module thisModule : modulesList) {
            sModulesList.add(thisModule.getName());
        }
        inputModule.getItems().addAll(sModulesList);
        lblCourseName.setText(selectedCourse.getName());
    }
    
    public void setCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
    
    public void display(String title) throws IOException, SQLException {
        Stage window = new Stage();
        window.getIcons().add(new Image(App.ICON_PATH));
        Scene scene = new Scene(App.loadFXML("LinkModule"), 450, 400);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        window.setScene(scene);
        window.showAndWait();
    }
        
    
    @FXML
    private void userDidClickCancel() throws IOException {
        Stage stage = (Stage) btnCancelLinkModule.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void userDidClickSave() throws IOException, SQLException {
        // validate all fields for input first
        // if all valid:
        // save all fields into their respective formats
        // call a database helper method to insert into course. 
        
        String moduleName = (String) inputModule.getValue();
        String courseCode = selectedCourse.getCourseCode();
        
        DatabaseHelper.insertIntoCoursesHaveModules(courseCode, moduleName);
        DatabaseHelper.sortModules(courseCode);
        
        // close the window
        Stage stage = (Stage) btnCancelLinkModule.getScene().getWindow();
        stage.close();
    }
}
