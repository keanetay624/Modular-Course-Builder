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
public class EditCourseController {
    
    @FXML
    Button btnCancelEditCourse;
    
    @FXML
    Button btnSaveEditCourse;
    
    @FXML
    TextField inputCourseCode;
    
    @FXML
    TextField inputCourseName;
    
    @FXML
    TextField inputFaculty;
    
    @FXML
    TextField inputSchool;
    
    @FXML
    TextField inputLevel;
    
    @FXML
    TextField inputCampus;
    
    private static Course selectedCourse;
    
    public void initialize() {
        inputCourseCode.setText(selectedCourse.getCourseCode());
        inputCourseCode.setDisable(true);
        inputCourseName.setText(selectedCourse.getName());
        inputFaculty.setText(selectedCourse.getFaculty());
        inputSchool.setText(selectedCourse.getSchool());
        inputLevel.setText(String.valueOf(selectedCourse.getLevel()));
        inputCampus.setText(selectedCourse.getCampus());
    }
    
    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
    
    public static void display(String title) throws IOException {
        Stage window = new Stage();
        window.getIcons().add(new Image(App.ICON_PATH));
        Scene scene = new Scene(App.loadFXML("EditCourse"), 450, 400);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        window.setScene(scene);
        window.showAndWait();
    }
    
    @FXML
    private void userDidClickCancel() throws IOException {
        Stage stage = (Stage) btnCancelEditCourse.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void userDidClickSave() throws IOException, SQLException {
        // validate all fields for input first
        // if all valid:
        // save all fields into their respective formats
        // call a database helper method to insert into course. 
        
        String courseCode = inputCourseCode.getText();
        String name = inputCourseName.getText();
        String faculty = inputFaculty.getText();
        String school = inputSchool.getText();
        int level = Integer.parseInt(inputLevel.getText());
        String campus = inputCampus.getText();
        
        Course newCourse = new Course(courseCode, name, faculty, school, level, campus, 0);
        
        // call a function from the database helper class
        // update Course where courseid = selected course id
        // set values (courseCode, name, faculty, school, level, campus)
        DatabaseHelper.updateCourse(selectedCourse, newCourse);
        
        // close the window
        Stage stage = (Stage) btnCancelEditCourse.getScene().getWindow();
        stage.close();
    }
}
