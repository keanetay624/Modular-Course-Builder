package com.mycompany.mavenproject1;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    Button btnCourseNew, btnCourseEdit, btnCourseArchive, btnCourseUpload, 
            btnCourseDownload, btnRefreshCourse;
    
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
        
        btnCourseEdit.setVisible(false);
        btnCourseDownload.setVisible(false);
        btnCourseArchive.setVisible(false);
    }
    
    /*
    * This function is called when a course is clicked within the TableView
    */
    @FXML
    private Course userDidSelectCourse() throws IOException, SQLException {
        Course selectedCourse = tblCourse.getSelectionModel().getSelectedItem();
        System.out.println(selectedCourse.getName() + " clicked!");
        btnCourseEdit.setVisible(true);
        btnCourseDownload.setVisible(true);
        btnCourseArchive.setVisible(true);
        return selectedCourse;
    }
    
    @FXML
    private void userDidArchiveCourse() throws IOException, SQLException {
        Course selectedCourse = tblCourse.getSelectionModel().getSelectedItem();
        tblCourse.getItems().removeAll(selectedCourse);
        System.out.println(selectedCourse.getName() + " removed!");
        DatabaseHelper.archiveCourse(selectedCourse);
        System.out.println(selectedCourse.getName() + " removed from database!");
        btnCourseEdit.setVisible(false);
        btnCourseDownload.setVisible(false);
        btnCourseArchive.setVisible(false);
    }

    @FXML
    private void switchToNewCourse() throws IOException {
        NewCourseController.display("New Course");
    }
    
    private void buildUI() throws IOException {
        TilePane tilePane = new TilePane();
    }
    
    @FXML
    public void refreshTable() throws SQLException {
        CourseHelper.getCourses();
        ObservableList<Course> newList = CourseHelper.getCourseObservableList();
        tblCourse.setItems(newList);
    }
    
    @FXML
    private void userDidClickSignOut() throws IOException {
        App.setRoot("login");
    }
    
}
