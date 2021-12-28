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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Keane Local
 */
public class NewResourceController {
    
    @FXML
    ComboBox inputSection;
    
    @FXML
    Button btnCancelNewResource;
    
    @FXML
    Button btnSaveNewResource;
    
    @FXML
    Button btnUpload;
    
    @FXML
    TextField inputName;
    
    public void initialize() throws SQLException {
        // initialize the combobox with list of available modules
        System.out.println("Test initialize method");
        ObservableList<Section> sectionsList = DatabaseHelper.getSections();
        ObservableList<String> ssectionsList = FXCollections.observableArrayList();
        
        for (Section thisSection : sectionsList) {
            ssectionsList.add(thisSection.getName());
        }
        inputSection.getItems().addAll(ssectionsList);
    }
    
    
    public static void display(String title) throws IOException {
        Stage window = new Stage();
        window.getIcons().add(new Image("file:src/main/resources/ModularCourseBuilder/unsw_1.jpg"));
        Scene scene = new Scene(App.loadFXML("NewResource"), 450, 400);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        window.setScene(scene);
        window.showAndWait();
    }
    
    @FXML
    private void userDidClickCancel() throws IOException {
        Stage stage = (Stage) btnCancelNewResource.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void userDidClickSave() throws IOException, SQLException {
        // validate all fields for input first
        // if all valid:
        // save all fields into their respective formats
        // call a database helper method to insert into course. 
        
        String resourceName = inputName.getText();
        String sectionName = (String) inputSection.getValue();
        Module dummyModule = new Module("","",0);
        Section dummySection = new Section(dummyModule, sectionName, "", 1,0);
        
        //NOTE: I've set the extension to null for now. Decide later if
        // we want to implement the extension here or under attachments. 
        Resource newResource = new Resource(dummySection, resourceName, "");
        DatabaseHelper.insertIntoResource(newResource);
        
        // close the window
        Stage stage = (Stage) btnCancelNewResource.getScene().getWindow();
        stage.close();
    }
}
