package ModularCourseBuilder;

import java.io.File;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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
    Label lblFileName;
    
    @FXML
    AnchorPane anchorPaneID;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Section> newList = DatabaseHelper.getSections();
        
        tblSection.getItems().clear();
        listResources.getItems().clear();
        lblFileName.setVisible(false);
        
        // Applying styling to selected nav button
        JavaFXHelper.setButtonsActive(new Button[]{navBtnHome, navBtnCourses, 
            navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes}, false);
        JavaFXHelper.setButtonActive(navBtnSections, true);
        
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
        
        String fileName = DatabaseHelper.getFileName(3, selectedSection.getName(), selectedSection.getsModule().getName());
        
        if (!fileName.equals("")) {
            lblFileName.setText("File available: " + fileName);
            JavaFXHelper.setNodeHidden(btnSectionDownload, false);
        } else {
            lblFileName.setText("No file available.");
            JavaFXHelper.setNodeHidden(btnSectionDownload, true);
        }
        lblFileName.setVisible(true);
        
        // handle Resources within the selected section
        listResources.getItems().clear();
        ObservableList<Resource> resourcesList = FXCollections.observableArrayList();
        ObservableList<String> sresourcesList = FXCollections.observableArrayList();
        resourcesList = DatabaseHelper.getResourcesWithinSection(selectedSection); // TODO: Write this function
        
        for (Resource thisResource : resourcesList) {
            sresourcesList.add(thisResource.getName());
        }
        
        listResources.getItems().addAll(sresourcesList);
        JavaFXHelper.setNodesHidden(new Node[]{btnSectionEdit, btnSectionArchive, btnSectionUpload}, false);
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
    private void userDidClickUpload() throws IOException, SQLException {
        Section selectedSection = tblSection.getSelectionModel().getSelectedItem();
        FileHelper fh = new FileHelper();
        fh.getFile(3, selectedSection.getName(), selectedSection.getsModule().getName());
    }
    
    @FXML
    private void userDidClickDownload() throws IOException, SQLException {
        //prompt the user to select a download location using a directorychooser
        Section selectedSection = tblSection.getSelectionModel().getSelectedItem();
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
        fh.getSectionBlob(selectedSection.getName(), selectedSection.getsModule().getName(), filepath);
    }

    @FXML
    private void switchToNewSection() throws IOException, SQLException {
        NewSectionController newController = new NewSectionController();
        newController.display("New Module");
        refreshTable();
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
