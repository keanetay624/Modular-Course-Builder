package com.mycompany.mavenproject1;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML
    TextField txtUsername;
    
    @FXML
    PasswordField txtPassword;
    
    @FXML
    Label outputLabel;
    
    @FXML
    private void initialize() {
        outputLabel.setVisible(false);
    }
    
    @FXML
    private void userDidClickLogin() throws IOException, SQLException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        
        if (Database.validateUser(username, password)) {
            App.setRoot("home");
        } else {
            outputLabel.setVisible(true);
            outputLabel.setText("Login failed: Incorrect Username or Password");
        }
        
    }
}