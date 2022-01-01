package ModularCourseBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        
        // Applying styling to selected nav button
        JavaFXHelper.setButtonsActive(new Button[]{navBtnHome, navBtnCourses, 
            navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes}, false);
        JavaFXHelper.setButtonActive(navBtnOutcomes, true);
        
        for (Outcome thisOutcome : newList) {
            tblOutcome.getItems().addAll(thisOutcome);
        }
        
        trOutcomeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trOutcomeDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        trModule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getoModule().getName()));
        trSequence.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        
        // Setting nodes to hidden.
        JavaFXHelper.setNodesHidden(new Node[]{btnOutcomeEdit, btnOutcomeArchive, btnOutcomeUpload, btnOutcomeDownload}, true);
        
        // Click Listener for course table.
        tblOutcome.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectOutcome(newValue);
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
    private void userDidSelectOutcome(Outcome selectedOutcome) throws IOException, SQLException {
        if (selectedOutcome == null) {
            return;
        }
        
        JavaFXHelper.setNodesHidden(new Node[]{btnOutcomeEdit, btnOutcomeArchive, btnOutcomeUpload, btnOutcomeDownload}, false);
    }
    
    @FXML
    private void userDidArchiveOutcome() throws IOException, SQLException {
        Outcome selectedOutcome = tblOutcome.getSelectionModel().getSelectedItem();
        tblOutcome.getItems().removeAll(selectedOutcome);
        DatabaseHelper.archiveOutcome(selectedOutcome);
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
    private void userDidEditOutcome() throws IOException, SQLException {
        System.out.println("Edit Outcome Clicked!");
        
        // write a database query to get the selected outcome from the database
        Outcome selectedOutcome = tblOutcome.getSelectionModel().getSelectedItem();
        EditOutcomeController emc = new EditOutcomeController();
        emc.setSelectedOutcome(selectedOutcome);
        emc.display("Edit Outcome");
        refreshTable();
    }
    
    @FXML
    private void switchToNewOutcome() throws IOException, SQLException {
        NewOutcomeController.display("New Outcome");
        refreshTable();
    }
    
    @FXML
    public void refreshTable() throws SQLException {
        ObservableList<Outcome> newList = DatabaseHelper.getOutcomes();
        tblOutcome.setItems(newList);
    }
    
}
