package com.mycompany.mavenproject1;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;

public class CourseViewerController {
    
    @FXML
    TableView<Course> tblCourse;
    
    @FXML
    TableColumn<Course, String> trCourseCode;
    
    @FXML
    TableColumn<Course, String> trCourseName;
    
    @FXML
    TableColumn<Course, String> trFaculty;
    
    @FXML
    TableColumn<Course, String> trSchool;
    
    @FXML
    TableColumn<Course, String> trCampus;
    
    @FXML
    TableColumn<Course, Integer> trLevel;
    
    @FXML
    private void initialize() throws SQLException {
        CourseHelper.getCourses();
        
        ObservableList<Course> coursesList = CourseHelper.getCourseObservableList();
        
        tblCourse.setItems(coursesList);
        
        trCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        trCourseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trFaculty.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        trSchool.setCellValueFactory(new PropertyValueFactory<>("school"));
        trCampus.setCellValueFactory(new PropertyValueFactory<>("campus"));
        trLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }
    
    private void buildUI() throws IOException {
        TilePane tilePane = new TilePane();
    }
}
