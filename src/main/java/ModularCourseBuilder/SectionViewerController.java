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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;

public class SectionViewerController {
    
    @FXML
    TableView<Section> tblSection;
    
    @FXML
    ListView listResources;
    
    @FXML
    TableColumn<Section, String> trSectionName;
    
    @FXML
    TableColumn<Section, String> trSectionDesc;
    
    @FXML
    TableColumn<Section, String> trModule;
    
    @FXML
    TableColumn<Section, Integer> trSequence;
    
    @FXML
    Button btnSectionNew, btnSectionEdit, btnSectionArchive, btnSectionUpload, 
            btnSectionDownload, btnRefreshSection;
    
    @FXML 
    Button navBtnHome ,navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes, navBtnSignOut;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Section> newList = DatabaseHelper.getSections();
        
        tblSection.getItems().clear();
        listResources.getItems().clear();
        
        for (Section thisSection : newList) {
            tblSection.getItems().addAll(thisSection);
        }
        
        trSectionName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trSectionDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        // Looked up binding nested properties at: 
        // https://stackoverflow.com/questions/24769296/binding-nested-object-properties-to-tableview-in-javafx
        trModule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getsModule().getName()));
        trSequence.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        
        // Setting nodes to hidden.
        JavaFXHelper.setNodesHidden(new Node[]{btnSectionEdit, btnSectionArchive, btnSectionUpload, btnSectionDownload}, true);
        
        // Click Listener for course table.
        tblSection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectSection(newValue);
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
    private void userDidSelectSection(Section selectedSection) throws IOException, SQLException {
        
        if (selectedSection == null) {
            return;
        }
        
        // handle Resources within the selected section
        listResources.getItems().clear();
        ObservableList<Resource> resourcesList = FXCollections.observableArrayList();
        ObservableList<String> sresourcesList = FXCollections.observableArrayList();
        resourcesList = DatabaseHelper.getResourcesWithinSection(selectedSection); // TODO: Write this function
        
        for (Resource thisResource : resourcesList) {
            sresourcesList.add(thisResource.getName());
        }
        
        listResources.getItems().addAll(sresourcesList);
        JavaFXHelper.setNodesHidden(new Node[]{btnSectionEdit, btnSectionArchive, btnSectionUpload, btnSectionDownload}, false);
    }
//    
    @FXML
    private void userDidArchiveSection() throws IOException, SQLException {
        Section selectedSection = tblSection.getSelectionModel().getSelectedItem();
        tblSection.getItems().removeAll(selectedSection);
        System.out.println(selectedSection.getName() + " removed!");
        DatabaseHelper.archiveSection(selectedSection);
        System.out.println(selectedSection.getName() + " removed from database!");
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
    private void switchToNewSection() throws IOException, SQLException {
        NewSectionController newController = new NewSectionController();
        newController.display("New Module");
    }
    
    @FXML
    private void userDidEditSection() throws IOException, SQLException {
        System.out.println("Edit Clicked!");
        Section selectedSection = tblSection.getSelectionModel().getSelectedItem();
        
        EditSectionController esc = new EditSectionController();
        esc.setSelectedSection(selectedSection);
        esc.display("Edit Section");
        refreshTable();
    }
    
    @FXML
    public void refreshTable() throws SQLException {
        ObservableList<Section> newList = DatabaseHelper.getSections();
        tblSection.setItems(newList);
    }
    
}
