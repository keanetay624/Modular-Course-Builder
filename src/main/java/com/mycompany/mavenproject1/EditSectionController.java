/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Keane Local
 */
public class EditSectionController {
    
    @FXML
    Button btnCancelEditSection;
    
    @FXML
    Button btnSaveEditSection;
    
    @FXML
    TextField inputName;
    
    @FXML
    TextField inputDesc;
    
    @FXML
    ComboBox inputModule;
    
    private static Section selectedSection;
    
    public void initialize() throws SQLException {
        // initialize the combobox with list of available modules
        System.out.println("Test initialize method");
        ObservableList<Module> modulesList = DatabaseHelper.getModules();
        ObservableList<String> sModulesList = FXCollections.observableArrayList();
        
        for (Module thisModule : modulesList) {
            sModulesList.add(thisModule.getName());
        }
        inputModule.getItems().addAll(sModulesList);
        
        // set the default to the last selected module
        inputName.setText(selectedSection.getName());
        inputDesc.setText(selectedSection.getDesc());
        inputModule.setValue(selectedSection.getsModule().getName());
    }
    
    public void setSelectedSection(Section selectedSection) {
        this.selectedSection = selectedSection;
    }
    
    public void display(String title) throws IOException, SQLException {
        Stage window = new Stage();
        window.getIcons().add(new Image("file:src/main/resources/com/mycompany/mavenproject1/unsw_1.jpg"));
        Scene scene = new Scene(App.loadFXML("EditSection"), 450, 400);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        window.setScene(scene);
        window.showAndWait();
    }
        
    
    @FXML
    private void userDidClickCancel() throws IOException {
        Stage stage = (Stage) btnCancelEditSection.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void userDidClickSave() throws IOException, SQLException {
        // validate all fields for input first
        // if all valid:
        // save all fields into their respective formats
        // call a database helper method to insert into course. 
        
        String moduleName = (String) inputModule.getValue();
        String name = inputName.getText();
        String desc = inputDesc.getText();
        
        
        int temp = 10; // TODO: Change this to get highest sequence number + 1
        Module tempModule = new Module(moduleName, "", 0);
        Section newSection = new Section(tempModule, name, desc, temp, 0);
//        DatabaseHelper.insertIntoSection(newSection);
        DatabaseHelper.updateSection(selectedSection, newSection);
        DatabaseHelper.sortSections(moduleName);
        
        // close the window
        Stage stage = (Stage) btnCancelEditSection.getScene().getWindow();
        stage.close();
    }
}
