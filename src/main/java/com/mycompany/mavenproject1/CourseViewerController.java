package com.mycompany.mavenproject1;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;

public class CourseViewerController {
    
    @FXML
    TableView<Course> tblCourse;
    
    @FXML
    ListView listModules;
    
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
            btnCourseDownload, btnRefreshCourse, 
            btnShiftModuleUp, btnShiftModuleDown;
    
    @FXML 
    Button navBtnHome, navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes, navBtnSignOut;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Course> newList = DatabaseHelper.getCourses();
        tblCourse.getItems().clear();
        listModules.getItems().clear();
        
        for (Course thisCourse : newList) {
            tblCourse.getItems().addAll(thisCourse);
        }
        
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
        
        // populate the modules associated with the course
        listModules.getItems().clear();
        ObservableList<Module> modulesList = FXCollections.observableArrayList();
        ObservableList<String> sModulesList = FXCollections.observableArrayList();
        modulesList = DatabaseHelper.getModulesWithinCourses(selectedCourse);
        
        DatabaseHelper.sortModules(selectedCourse.getName());
        for (Module thisModule : modulesList) {
            sModulesList.add(thisModule.getName());
        }
        
        listModules.getItems().addAll(sModulesList);
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
        
        listModules.getItems().clear();
        btnCourseEdit.setVisible(false);
        btnCourseDownload.setVisible(false);
        btnCourseArchive.setVisible(false);
    }
    
    @FXML
    private void userDidShiftModuleUp() throws IOException, SQLException {
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        String selectedModule = (String) listModules.getSelectionModel().getSelectedItem();
        System.out.println(selectedModule);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftModuleUp(selectedModule, selectedCourse);
        refreshTable();
    }
    
    @FXML
    private void userDidShiftModuleDown() throws IOException, SQLException {
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        String selectedModule = (String) listModules.getSelectionModel().getSelectedItem();
        System.out.println(selectedModule);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftModuleDown(selectedModule, selectedCourse);
        refreshTable();
    }
    
    @FXML
    private void userDidRemoveModule() throws IOException, SQLException {
        System.out.println("Remove Clicked!");
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        String selectedModule = (String) listModules.getSelectionModel().getSelectedItem();
        System.out.println(selectedModule);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.unlinkCourseModule(selectedModule, selectedCourse);
        refreshTable();
    }
    
    /* 
    * Navigation Functions
    */

    @FXML 
    private void switchToHome() throws IOException {
        App.setRoot("home");
    }
    
    @FXML
    private void switchToCourseViewer() throws IOException {
        App.setRoot("courseViewer");
    }
    
    @FXML
    private void switchToModuleViewer() throws IOException {
        App.setRoot("moduleViewer");
    }
    
    @FXML 
    private void switchToSectionViewer() throws IOException {
        App.setRoot("sectionViewer");
    }
    
    @FXML 
    private void switchToResourceViewer() throws IOException {
        App.setRoot("resourceViewer");
    }
    
    @FXML
    private void switchToOutcomeViewer() throws IOException {
        App.setRoot("OutcomeViewer");
    }
    
    @FXML
    private void userDidClickSignOut() throws IOException {
        App.setRoot("login");
    }
    
    /*
    * Auxilary Functions
    */
    
    @FXML
    private void switchToNewCourse() throws IOException {
        NewCourseController.display("New Course");
    }
    
    
    
    @FXML
    private void userDidAddModule() throws IOException, SQLException {
        System.out.println("Add Clicked!");
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        
        LinkModuleController lmc = new LinkModuleController();
        lmc.setCourse(selectedCourse);
        lmc.display("New Course");
        refreshTable();
    }
    
    @FXML
    public void refreshTable() throws SQLException {
        
        // clear all tables and lists
        tblCourse.getItems().clear();
        listModules.getItems().clear();
        
        // reset the course table
        ObservableList<Course> newList = DatabaseHelper.getCourses();
        tblCourse.setItems(newList);
        // select the first row of the new table by default.
        tblCourse.getSelectionModel().select(0);
        
        // reset the modules List
        ObservableList<Module> modulesList = FXCollections.observableArrayList();
        ObservableList<String> sModulesList = FXCollections.observableArrayList();
        modulesList = DatabaseHelper.getModulesWithinCourses(tblCourse.getSelectionModel().getSelectedItem());
        
        DatabaseHelper.sortModules(tblCourse.getSelectionModel().getSelectedItem().getName());
        for (Module thisModule : modulesList) {
            sModulesList.add(thisModule.getName());
        }
        listModules.setItems(sModulesList);
       
    }
    
}
