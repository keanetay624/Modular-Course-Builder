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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Keane Local
 */
public class NewUserController {
    
    @FXML
    Button btnCancelNewUser;
    
    @FXML
    Button btnSaveNewUser;
    
    @FXML
    TextField inputUsername;
    
    @FXML
    PasswordField inputPassword;
    
    @FXML
    Label outputLabel;
    
    public void initialize() throws SQLException {
        // initialize the combobox with list of available modules
        System.out.println("Test initialize method");
        outputLabel.setVisible(false);
        
    }
    
    public void display(String title) throws IOException, SQLException {
        Stage window = new Stage();
        window.getIcons().add(new Image("file:src/main/resources/ModularCourseBuilder/unsw_1.jpg"));
        Scene scene = new Scene(App.loadFXML("NewUser"), 450, 400);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        window.setScene(scene);
        window.showAndWait();
    }
        
    
    @FXML
    private void userDidClickCancel() throws IOException {
        Stage stage = (Stage) btnCancelNewUser.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void userDidClickSave() throws IOException, SQLException {
        // validate all fields for input first
        // if all valid:
        // save all fields into their respective formats
        // call a database helper method to insert into course. 
        
        String username = inputUsername.getText();
        String password = inputPassword.getText();
        
        // check that username is not empty
        if (username.equals("")) {
            outputLabel.setText("Username cannot be empty");
            outputLabel.setVisible(true);
        } 
        // check that username does not already exist
        else if (DatabaseHelper.userExists(username)){
            outputLabel.setText("User already exists");
            outputLabel.setVisible(true);
        }
        // check that password is not empty
        else if (password.equals("")) {
            outputLabel.setText("Password cannot be empty");
            outputLabel.setVisible(true);
        } 
        // validate if password is at least 8 characters long
        else if (password.length() < 8) {
            outputLabel.setText("Password must have at least 8 characters");
            outputLabel.setVisible(true);
        } 
        // validate if password contains at least 1 special character
        else if (countSpecialCharacters(password) < 1) {
            outputLabel.setText("Password must have at least 1 special character");
            outputLabel.setVisible(true);
        } 
        // finally, insert into database. 
        else {
            DatabaseHelper.insertIntoUser(username, password); 
            Stage stage = (Stage) btnCancelNewUser.getScene().getWindow();
            stage.close();
        }
        
    }
    
        private int countSpecialCharacters(String s) {
        String specialCharacters = "!#$%&'()*+,./:;<=>?@[]^_`{|}~";
        String ss[] = s.split("");
        int count = 0;
        for (int i = 0; i < ss.length; i++) {
            if (specialCharacters.contains(ss[i])) {
                count++;
            }
        }
        return count;
    }
}
