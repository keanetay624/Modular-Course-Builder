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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ModuleViewerController {
    
    @FXML
    TableView<Module> tblModule;
    
    @FXML
    TableColumn<Module, String> trModuleName;
    
    @FXML
    TableColumn<Module, String> trModuleDesc;
    
    @FXML
    ListView listSections, listOutcomes;
    
    
    @FXML
    Button btnModuleNew, btnModuleEdit, btnModuleArchive, btnModuleUpload, 
            btnModuleDownload, btnRefreshModule,
            btnShiftSectionUp, btnShiftSectionDown,
            btnShiftOutcomeUp, btnShiftOutcomeDown;
    
    @FXML 
    Button navBtnHome ,navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes, navBtnSignOut;
    
    @FXML
    Label lblFileName;
    
    @FXML
    AnchorPane anchorPaneID;
    
    @FXML
    TextField searchbox;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Module> newList = DatabaseHelper.getModules();
        
        tblModule.getItems().clear();
        listSections.getItems().clear();
        listOutcomes.getItems().clear();
        lblFileName.setVisible(false);
        
        // Applying styling to selected nav button
        JavaFXHelper.setButtonsActive(new Button[]{navBtnHome, navBtnCourses, 
            navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes}, false);
        JavaFXHelper.setButtonActive(navBtnModules, true);
        
        for (Module thisModule : newList) {
            tblModule.getItems().addAll(thisModule);
        }
        
        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true); 
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
        
        trModuleName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trModuleDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        
        btnModuleEdit.setVisible(false);
        btnModuleDownload.setVisible(false);
        btnModuleArchive.setVisible(false);
        
        // Setting nodes to hidden.
        JavaFXHelper.setNodesHidden(new Node[]{btnModuleEdit, btnModuleArchive, btnModuleUpload, btnModuleDownload}, true);
        
        // Click Listener for course table.
        tblModule.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectModule(newValue);
            } catch (IOException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Click Listener for sections listview.
        listSections.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectSection((String) newValue);
            } catch (IOException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Click Listener for outcomes listview.
        listOutcomes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectOutcome((String) newValue);
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
    private void userDidSelectModule(Module selectedModule) throws IOException, SQLException {
        if (selectedModule == null) {
            return;
        }
        
        String fileName = DatabaseHelper.getFileName(2, selectedModule.getName(),"");
        
        if (!fileName.equals("")) {
            lblFileName.setText("File available: " + fileName);
            JavaFXHelper.setNodeHidden(btnModuleDownload, false);
        } else {
            lblFileName.setText("No file available.");
            JavaFXHelper.setNodeHidden(btnModuleDownload, true);
        }
        lblFileName.setVisible(true);
        
        // handle sections within the selected module
        listSections.getItems().clear();
        ObservableList<Section> sectionsList = FXCollections.observableArrayList();
        ObservableList<String> sSectionsList = FXCollections.observableArrayList();
        sectionsList = DatabaseHelper.getSectionsWithinModule(selectedModule);
        
        DatabaseHelper.sortSections(selectedModule.getName());
        for (Section thisSection : sectionsList) {
            sSectionsList.add(thisSection.getName());
        }
        
        // handle outcomes within the selected module
        listOutcomes.getItems().clear();
        ObservableList<Outcome> outcomesList = FXCollections.observableArrayList();
        ObservableList<String> sOutcomesList = FXCollections.observableArrayList();
        outcomesList = DatabaseHelper.getOutcomesWithinModule(selectedModule);
        
        DatabaseHelper.sortOutcomes(selectedModule.getName());
        for (Outcome thisOutcome : outcomesList) {
            sOutcomesList.add(thisOutcome.getName());
        }
        
        listSections.getItems().addAll(sSectionsList);
        listOutcomes.getItems().addAll(sOutcomesList);
        
        // Enable add module button
//        btnAddSection.setDisable(false); // add this button later

        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true);
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
        
        JavaFXHelper.setNodesHidden(new Node[]{btnModuleEdit, btnModuleArchive, btnModuleUpload}, false);
    }
    
    @FXML
    private void userDidSelectSection(String selectedSection) throws IOException, SQLException {
        // handle null in the event of an edit window being closed.
        if (selectedSection == null) {
            return;
        }
        
        // Enable respective buttons
        btnShiftSectionUp.setDisable(false); 
        btnShiftSectionDown.setDisable(false); 
    }
    
    //todo finish this method
    @FXML
    private void userDidSelectOutcome(String selectedOutcome) throws IOException, SQLException {
        // handle null in the event of an edit window being closed.
        if (selectedOutcome == null) {
            return;
        }
        // Enable respective buttons
        btnShiftOutcomeUp.setDisable(false); 
        btnShiftOutcomeDown.setDisable(false); 
    }
    

    @FXML
    private void userDidArchiveModule() throws IOException, SQLException {
        Module selectedModule = tblModule.getSelectionModel().getSelectedItem();
        tblModule.getItems().removeAll(selectedModule);
        System.out.println(selectedModule.getName() + " removed!");
        DatabaseHelper.archiveModule(selectedModule);
        System.out.println(selectedModule.getName() + " removed from database!");
        btnModuleEdit.setVisible(false);
        btnModuleDownload.setVisible(false);
        btnModuleArchive.setVisible(false);
        
        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true); 
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
    }
    
    @FXML
    private void userDidShiftSectionUp() throws IOException, SQLException {
        String selectedSection = listSections.getSelectionModel().getSelectedItem().toString();
        String selectedModule = tblModule.getSelectionModel().getSelectedItem().getName();
        System.out.println(selectedSection);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftSectionUp(selectedSection, selectedModule);
        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true); 
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
        refreshTable();
    }
    
    @FXML
    private void userDidShiftSectionDown() throws IOException, SQLException {
        String selectedSection = listSections.getSelectionModel().getSelectedItem().toString();
        String selectedModule = tblModule.getSelectionModel().getSelectedItem().getName();
        System.out.println(selectedSection);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftSectionDown(selectedSection, selectedModule);
        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true); 
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
        refreshTable();
    }
    
    @FXML
    private void userDidShiftOutcomeUp() throws IOException, SQLException {
        String selectedOutcome = listOutcomes.getSelectionModel().getSelectedItem().toString();
        String selectedModule = tblModule.getSelectionModel().getSelectedItem().getName();
        System.out.println(selectedOutcome);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftOutcomeUp(selectedOutcome, selectedModule);
        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true); 
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
        refreshTable();
    }
    
    @FXML
    private void userDidShiftOutcomeDown() throws IOException, SQLException {
        String selectedOutcome = listOutcomes.getSelectionModel().getSelectedItem().toString();
        String selectedModule = tblModule.getSelectionModel().getSelectedItem().getName();
        System.out.println(selectedOutcome);
        
        // send this to the database helper class
        // get the id of the previous section (if any) 
        // if there is previous section, swap the order
        // else do nothing
        DatabaseHelper.shiftOutcomeDown(selectedOutcome, selectedModule);
        // Disable respective buttons
        btnShiftSectionUp.setDisable(true); 
        btnShiftSectionDown.setDisable(true); 
        btnShiftOutcomeUp.setDisable(true); 
        btnShiftOutcomeDown.setDisable(true); 
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
    private void userDidClickSearch() throws SQLException {
        String query = searchbox.getText();
        System.out.println(query);
        
        if (!query.equals("")) {
            tblModule.setItems(DatabaseHelper.searchModule(query));
        } else {
            tblModule.setItems(DatabaseHelper.getModules());
        }
    }
    
    @FXML
    private void switchToNewModule() throws IOException, SQLException {
        NewModuleController.display("New Module");
        refreshTable();
    }
    
    @FXML
    private void userDidEditModule() throws IOException, SQLException {
        System.out.println("Edit Clicked!");
        Module selectedModule = tblModule.getSelectionModel().getSelectedItem();
        
        EditModuleController emc = new EditModuleController();
        emc.setSelectedModule(selectedModule);
        emc.display("Edit Module");
        refreshTable();
    }
    
    @FXML
    private void userDidClickUpload() throws IOException, SQLException {
        String selectedModule = tblModule.getSelectionModel().getSelectedItem().getName();
        FileHelper fh = new FileHelper();
        fh.getFile(2, selectedModule, "");
    }
    
    @FXML
    private void userDidClickDownload() throws IOException, SQLException {
        //prompt the user to select a download location using a directorychooser
        String selectedModule = tblModule.getSelectionModel().getSelectedItem().getName();
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
        fh.getModuleBlob(selectedModule, filepath);
    }
    
    @FXML
    public void refreshTable() throws SQLException {

        // clear all tables and lists
        tblModule.getItems().clear();
        listSections.getItems().clear();
        listOutcomes.getItems().clear();
        
        // reset the tableview
        ObservableList<Module> newList = DatabaseHelper.getModules();
        tblModule.setItems(newList);
        // select the first row of the new table by default.
        tblModule.getSelectionModel().select(0);
        
        // reset the sectionsList
        ObservableList<Section> sectionsList = FXCollections.observableArrayList();
        ObservableList<String> sSectionsList = FXCollections.observableArrayList();
        sectionsList = DatabaseHelper.getSectionsWithinModule(tblModule.getSelectionModel().getSelectedItem());
        
        DatabaseHelper.sortSections(tblModule.getSelectionModel().getSelectedItem().getName());
        for (Section thisSection : sectionsList) {
            sSectionsList.add(thisSection.getName());
        }
        listSections.setItems(sSectionsList);
        
        // reset the outcomes list
        ObservableList<Outcome> outcomesList = FXCollections.observableArrayList();
        ObservableList<String> sOutcomesList = FXCollections.observableArrayList();
        outcomesList = DatabaseHelper.getOutcomesWithinModule(tblModule.getSelectionModel().getSelectedItem());
        
        DatabaseHelper.sortOutcomes(tblModule.getSelectionModel().getSelectedItem().getName());
        for (Outcome thisOutcome : outcomesList) {
            sOutcomesList.add(thisOutcome.getName());
        }
        
        listOutcomes.setItems(sOutcomesList);
    }
    
}
