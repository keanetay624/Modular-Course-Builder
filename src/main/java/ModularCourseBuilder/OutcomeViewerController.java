package ModularCourseBuilder;

import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;

public class OutcomeViewerController {
    
    @FXML
    TableView<Outcome> tblOutcome;
    
    @FXML
    TableColumn<Outcome, String> trModule;
    
    @FXML
    TableColumn<Outcome, String> trOutcomeName;
    
    @FXML
    TableColumn<Outcome, String> trOutcomeDesc;
    
    @FXML
    TableColumn<Outcome, Integer> trSequence;
    
    @FXML
    Button btnOutcomeNew, btnOutcomeEdit, btnOutcomeArchive, btnOutcomeUpload, 
            btnOutcomeDownload, btnRefreshOutcome;
    
    @FXML 
    Button navBtnHome, navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes, navBtnSignOut;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Outcome> newList = DatabaseHelper.getOutcomes();
        tblOutcome.getItems().clear();
        
        for (Outcome thisOutcome : newList) {
            tblOutcome.getItems().addAll(thisOutcome);
        }
        
        trOutcomeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trOutcomeDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        trModule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getoModule().getName()));
        trSequence.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        
        btnOutcomeEdit.setVisible(false);
        btnOutcomeDownload.setVisible(false);
        btnOutcomeArchive.setVisible(false);
    }
    
    /*
    * This function is called when a course is clicked within the TableView
    */
    @FXML
    private Outcome userDidSelectOutcome() throws IOException, SQLException {
        Outcome selectedOutcome = tblOutcome.getSelectionModel().getSelectedItem();
        System.out.println(selectedOutcome.getName() + " clicked!");
        btnOutcomeEdit.setVisible(true);
        btnOutcomeDownload.setVisible(true);
        btnOutcomeArchive.setVisible(true);
        return selectedOutcome;
    }
    
    @FXML
    private void userDidArchiveOutcome() throws IOException, SQLException {
        Outcome selectedOutcome = tblOutcome.getSelectionModel().getSelectedItem();
        tblOutcome.getItems().removeAll(selectedOutcome);
        System.out.println(selectedOutcome.getName() + " removed!");
        DatabaseHelper.archiveOutcome(selectedOutcome);
        System.out.println(selectedOutcome.getName() + " removed from database!");
        btnOutcomeEdit.setVisible(false);
        btnOutcomeDownload.setVisible(false);
        btnOutcomeArchive.setVisible(false);
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
    private void switchToNewOutcome() throws IOException, SQLException {
        NewOutcomeController.display("New Outcome");
    }
    
    @FXML
    public void refreshTable() throws SQLException {
        ObservableList<Outcome> newList = DatabaseHelper.getOutcomes();
        tblOutcome.setItems(newList);
    }
    
}
