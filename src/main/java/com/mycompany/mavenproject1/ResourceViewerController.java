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
import javafx.scene.media.MediaView;

public class ResourceViewerController {
    
    @FXML
    TableView<Resource> tblResource;
    
    @FXML
    TableColumn<Resource, String> trResourceName;
    
    @FXML
    TableColumn<Resource, String> trResourceType;
    
    @FXML
    MediaView mvResource;
    
    @FXML
    Button btnResourceNew, btnResourceEdit, btnResourceArchive, btnResourceUpload, 
            btnResourceDownload, btnRefreshResource;
    
    @FXML 
    Button navBtnHome,navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnSignOut;
    
//    @FXML
//    private void initialize() throws SQLException {
//        ObservableList<Resource> newList = DatabaseHelper.getResources();
//        
//        tblModule.getItems().clear();
//        
//        for (Module thisModule : newList) {
//            tblModule.getItems().addAll(thisModule);
//        }
//        
//        trModuleName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        trModuleDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
//        
//        btnModuleEdit.setVisible(false);
//        btnModuleDownload.setVisible(false);
//        btnModuleArchive.setVisible(false);
//    }
    
    /*
    * This function is called when a course is clicked within the TableView
    */
//    @FXML
//    private Module userDidSelectModule() throws IOException, SQLException {
//        Module selectedModule = tblModule.getSelectionModel().getSelectedItem();
//        System.out.println(selectedModule.getName() + " clicked!");
//        btnModuleEdit.setVisible(true);
//        btnModuleDownload.setVisible(true);
//        btnModuleArchive.setVisible(true);
//        return selectedModule;
//    }
////    
//    @FXML
//    private void userDidArchiveModule() throws IOException, SQLException {
//        Module selectedModule = tblModule.getSelectionModel().getSelectedItem();
//        tblModule.getItems().removeAll(selectedModule);
//        System.out.println(selectedModule.getName() + " removed!");
//        DatabaseHelper.archiveModule(selectedModule);
//        System.out.println(selectedModule.getName() + " removed from database!");
//        btnModuleEdit.setVisible(false);
//        btnModuleDownload.setVisible(false);
//        btnModuleArchive.setVisible(false);
//    }
    
    /*
    * Nav Functions
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
    private void userDidClickSignOut() throws IOException {
        App.setRoot("login");
    }
    
    /*
    * Auxilary Functions
    */
    
    @FXML
    private void switchToNewModule() throws IOException {
        NewModuleController.display("New Module");
    }
    
//    @FXML
//    public void refreshTable() throws SQLException {
//        ObservableList<Module> newList = DatabaseHelper.getModules();
//        tblModule.setItems(newList);
//    }
    
}
