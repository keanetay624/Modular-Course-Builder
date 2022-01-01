package ModularCourseBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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
            btnShiftModuleUp, btnShiftModuleDown, btnAddModule, btnRemoveModule;
    
    @FXML 
    Button navBtnHome, navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes, navBtnSignOut;
    
    @FXML
    Label lblFileName;
    
    @FXML
    AnchorPane anchorPaneID;
    
    public Course currentlySelectedCourse;
    public String currentlySelectedModule;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Course> newList = DatabaseHelper.getCourses();
        tblCourse.getItems().clear();
        listModules.getItems().clear();
        lblFileName.setVisible(false);
        
        // Applying styling to selected nav button
        JavaFXHelper.setButtonsActive(new Button[]{navBtnHome, navBtnCourses, 
            navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes}, false);
        JavaFXHelper.setButtonActive(navBtnCourses, true);
        
        for (Course thisCourse : newList) {
            tblCourse.getItems().addAll(thisCourse);
        }
        
        trCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        trCourseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trFaculty.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        trSchool.setCellValueFactory(new PropertyValueFactory<>("school"));
        trCampus.setCellValueFactory(new PropertyValueFactory<>("campus"));
        trLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        
        // Setting nodes to hidden.
        JavaFXHelper.setNodesHidden(new Node[]{btnCourseEdit, btnCourseArchive, btnCourseUpload, btnCourseDownload}, true);
        
        // adding disable function
        btnShiftModuleUp.setDisable(true);
        btnShiftModuleDown.setDisable(true);
        btnAddModule.setDisable(true);
        btnRemoveModule.setDisable(true);
        
        // Click Listener for course table.
        tblCourse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectCourse(newValue);
            } catch (IOException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        listModules.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectModule((String)newValue);
            } catch (IOException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /*
    * This function is called when a course is clicked within the TableView
    */
    @FXML
    private void userDidSelectCourse(Course selectedCourse) throws IOException, SQLException {
        // handle null in the event of an edit window being closed.
        if (selectedCourse == null) {
            return;
        }
        
        // adding disable function
        btnShiftModuleUp.setDisable(true);
        btnShiftModuleDown.setDisable(true);
        btnAddModule.setDisable(true);
        btnRemoveModule.setDisable(true);
        
        // check for an associated file with selected course
        // if exists, notify the user that a file is available to download
        // else hide download button
        String fileName = DatabaseHelper.getFileName(1, selectedCourse.getName(),"");
        
        if (!fileName.equals("")) {
            lblFileName.setText("File available: " + fileName);
            JavaFXHelper.setNodeHidden(btnCourseDownload, false);
        } else {
            lblFileName.setText("No file available.");
            JavaFXHelper.setNodeHidden(btnCourseDownload, true);
        }
        lblFileName.setVisible(true);
        
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
        JavaFXHelper.setNodesHidden(new Node[]{btnCourseEdit, btnCourseArchive, btnCourseUpload}, false);
        
        // Enable add module button
        btnAddModule.setDisable(false);
        
        this.currentlySelectedCourse = selectedCourse;
    }
    
    @FXML
    private void userDidSelectModule(String selectedModule) throws IOException, SQLException {
        // handle null in the event of an edit window being closed.
        if (selectedModule == null) {
            return;
        }
        
        // Enable relevant buttons
        btnRemoveModule.setDisable(false);
        btnShiftModuleUp.setDisable(false);
        btnShiftModuleDown.setDisable(false);
        
//        JavaFXHelper.setNodesHidden(new Node[]{btnShiftModuleUp, btnShiftModuleDown, btnRemoveModule}, false); // don't need this anymore
        this.currentlySelectedModule = selectedModule;
    }
    
    @FXML
    private void userDidArchiveCourse() throws IOException, SQLException {
        Course selectedCourse = tblCourse.getSelectionModel().getSelectedItem();
        tblCourse.getItems().removeAll(selectedCourse);
        DatabaseHelper.archiveCourse(selectedCourse);
        
        listModules.getItems().clear();
        btnCourseEdit.setVisible(false);
        btnCourseDownload.setVisible(false);
        btnCourseArchive.setVisible(false);
    }
    
    @FXML
    private void userDidShiftModuleUp() throws IOException, SQLException {
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        String selectedModule = (String) listModules.getSelectionModel().getSelectedItem();
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftModuleUp(selectedModule, selectedCourse);
        btnShiftModuleUp.setDisable(true);
        btnShiftModuleDown.setDisable(true);
        btnAddModule.setDisable(true);
        btnRemoveModule.setDisable(true);
        refreshTable();
    }
    
    @FXML
    private void userDidShiftModuleDown() throws IOException, SQLException {
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        String selectedModule = (String) listModules.getSelectionModel().getSelectedItem();
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftModuleDown(selectedModule, selectedCourse);
        btnShiftModuleUp.setDisable(true);
        btnShiftModuleDown.setDisable(true);
        btnAddModule.setDisable(true);
        btnRemoveModule.setDisable(true);
        refreshTable();
    }
    
    @FXML
    private void userDidRemoveModule() throws IOException, SQLException {
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        String selectedModule = (String) listModules.getSelectionModel().getSelectedItem();
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.unlinkCourseModule(selectedModule, selectedCourse);
        btnShiftModuleUp.setDisable(true);
        btnShiftModuleDown.setDisable(true);
        btnAddModule.setDisable(true);
        btnRemoveModule.setDisable(true);
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
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        
        LinkModuleController lmc = new LinkModuleController();
        lmc.setCourse(selectedCourse);
        lmc.display("Link a Module");
        btnShiftModuleUp.setDisable(true);
        btnShiftModuleDown.setDisable(true);
        btnAddModule.setDisable(true);
        btnRemoveModule.setDisable(true);
        refreshTable();
    }
    
    @FXML
    private void userDidClickUpload() throws IOException, SQLException {
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        FileHelper fh = new FileHelper();
        fh.getFile(1, selectedCourse, "");
    }
    
    @FXML
    private void userDidClickDownload() throws IOException, SQLException {
        //prompt the user to select a download location using a directorychooser
        String selectedCourse = tblCourse.getSelectionModel().getSelectedItem().getName();
        final DirectoryChooser dc = new DirectoryChooser();
        String filepath = "";
        dc.setTitle("Select download location");

        Stage stage = (Stage) anchorPaneID.getScene().getWindow();

        File file = dc.showDialog(stage);

        if (file != null) {
            filepath = file.getAbsolutePath();
            System.out.println("Directory: " + filepath);
        }
        
        FileHelper fh = new FileHelper();
        fh.getCourseBlob(selectedCourse, filepath);
    }
    
    @FXML
    private void userDidEditCourse() throws IOException, SQLException {
        Course selectedCourse = tblCourse.getSelectionModel().getSelectedItem();
        
        EditCourseController ecc = new EditCourseController();
        ecc.setSelectedCourse(selectedCourse);
        ecc.display("Edit Course");
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
