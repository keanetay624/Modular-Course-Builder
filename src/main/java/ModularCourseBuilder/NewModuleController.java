/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class NewModuleController {
    
    @FXML
    Button btnCancelNewModule;
    
    @FXML
    Button btnSaveNewModule;
    
    @FXML
    TextField inputModuleName;
    
    @FXML
    TextField inputDesc;
    
    public static void display(String title) throws IOException {
        Stage window = new Stage();
        window.getIcons().add(new Image(App.ICON_PATH));
        Scene scene = new Scene(App.loadFXML("NewModule"), 450, 400);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        window.setScene(scene);
        window.showAndWait();
    }
    
    @FXML
    private void userDidClickCancel() throws IOException {
        Stage stage = (Stage) btnCancelNewModule.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void userDidClickSave() throws IOException, SQLException {
        // validate all fields for input first
        // if all valid:
        // save all fields into their respective formats
        // call a database helper method to insert into course. 
        
        String name = inputModuleName.getText();
        String desc = inputDesc.getText();
        
        Module newModule = new Module(name, desc, 0);
        DatabaseHelper.insertIntoModule(newModule);
        
        // close the window
        Stage stage = (Stage) btnCancelNewModule.getScene().getWindow();
        stage.close();
    }
}
