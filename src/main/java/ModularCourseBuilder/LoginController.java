package ModularCourseBuilder;

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
    
    private static String message;
    
    @FXML
    private void userDidClickLogin() throws IOException, SQLException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        
        // handle empty fields
        if (username.equals("")) {
            outputLabel.setText("Login failed: Username cannot be empty");
            outputLabel.setVisible(true);
        } else if (password.equals("")) {
            outputLabel.setText("Login failed: Password cannot be empty");
            outputLabel.setVisible(true);
        } else if (!passwordIsValid(password)) {
            outputLabel.setText(message);
            outputLabel.setVisible(true);
        }
        
        else {
           if (Database.validateUser(username, password)) {
            App.setRoot("home");
            } else {
                outputLabel.setVisible(true);
                outputLabel.setText("Login failed: Incorrect Username or Password");
            } 
        }
    }
    
    private boolean passwordIsValid(String password) {
        if (password.length() < 8) {
           message = "Password must be at least 8 characters.";
           return false;
        } 
        return true;
    }
    
}