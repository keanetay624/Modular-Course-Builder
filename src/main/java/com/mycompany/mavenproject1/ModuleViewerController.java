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
    private void initialize() throws SQLException {
        ObservableList<Module> newList = DatabaseHelper.getModules();
        
        tblModule.getItems().clear();
        listSections.getItems().clear();
        listOutcomes.getItems().clear();
        
        for (Module thisModule : newList) {
            tblModule.getItems().addAll(thisModule);
        }
        
        trModuleName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trModuleDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        
        btnModuleEdit.setVisible(false);
        btnModuleDownload.setVisible(false);
        btnModuleArchive.setVisible(false);
    }
    
    /*
    * This function is called when a course is clicked within the TableView
    */
    @FXML
    private Module userDidSelectModule() throws IOException, SQLException {
        Module selectedModule = tblModule.getSelectionModel().getSelectedItem();
        System.out.println(selectedModule.getName() + " clicked!");
        
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
        btnModuleEdit.setVisible(true);
        btnModuleDownload.setVisible(true);
        btnModuleArchive.setVisible(true);
        return selectedModule;
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
    private void switchToNewModule() throws IOException {
        NewModuleController.display("New Module");
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
    private void userDidEditOutcome() throws IOException, SQLException {
        System.out.println("Edit Outcome Clicked!");
        String outcomeString = listOutcomes.getSelectionModel().getSelectedItem().toString();
        Module selectedModule = tblModule.getSelectionModel().getSelectedItem();
        
        // write a database query to get the selected outcome from the database
        Outcome selectedOutcome = DatabaseHelper.getSelectedOutcome(selectedModule, outcomeString);
        EditOutcomeController emc = new EditOutcomeController();
        emc.setSelectedOutcome(selectedOutcome);
        emc.display("Edit Module");
        refreshTable();
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
