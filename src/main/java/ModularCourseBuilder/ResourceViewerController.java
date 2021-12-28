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
import javafx.scene.media.MediaView;

public class ResourceViewerController {
    
    @FXML
    TableView<Resource> tblResource;
    
    @FXML
    TableColumn<Resource, String> trSection;
    
    @FXML
    TableColumn<Resource, String> trResourceName;
    
    @FXML
    TableColumn<Resource, String> trResourceExt;
    
    @FXML
    MediaView mvResource;
    
    @FXML
    Button btnResourceNew, btnResourceEdit, btnResourceArchive, btnResourceUpload, 
            btnResourceDownload, btnRefreshResource;
    
    @FXML 
    Button navBtnHome ,navBtnCourses, navBtnModules, navBtnSections, 
            navBtnResources, navBtnOutcomes, navBtnSignOut;
    
    @FXML
    private void initialize() throws SQLException {
        ObservableList<Resource> newList = DatabaseHelper.getResources();
        
        tblResource.getItems().clear();
        
        for (Resource thisResource : newList) {
            tblResource.getItems().addAll(thisResource);
        }
        
        trSection.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getrSection().getName()));
        trResourceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trResourceExt.setCellValueFactory(new PropertyValueFactory<>("ext"));
        
        // Setting nodes to hidden.
        JavaFXHelper.setNodesHidden(new Node[]{btnResourceEdit, btnResourceArchive, btnResourceUpload, btnResourceDownload}, true);
        
        // Click Listener for course table.
        tblResource.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                userDidSelectResource(newValue);
            } catch (IOException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CourseViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /*
    * This function is called when a Resource is clicked within the TableView
    */
    @FXML
    private void userDidSelectResource(Resource selectedResource) throws IOException, SQLException {
        if (selectedResource == null) {
            return;
        }
        
        JavaFXHelper.setNodesHidden(new Node[]{btnResourceEdit, btnResourceArchive, btnResourceUpload, btnResourceDownload}, false);
    }
//    
    @FXML
    private void userDidArchiveResource() throws IOException, SQLException {
        Resource selectedResource = tblResource.getSelectionModel().getSelectedItem();
        tblResource.getItems().removeAll(selectedResource);
        System.out.println(selectedResource.getName() + " removed!");
        DatabaseHelper.archiveResource(selectedResource);
        System.out.println(selectedResource.getName() + " removed from database!");
        btnResourceEdit.setVisible(false);
        btnResourceDownload.setVisible(false);
        btnResourceArchive.setVisible(false);
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
    private void switchToNewResource() throws IOException {
        NewResourceController.display("New Resource");
    }
    
    @FXML
    private void userDidEditResource() throws IOException, SQLException {
        System.out.println("Edit Clicked!");
        Resource selectedResource = tblResource.getSelectionModel().getSelectedItem();
        
        EditResourceController erc = new EditResourceController();
        erc.setSelectedResource(selectedResource);
        erc.display("Edit Resource");
        refreshTable();
    }
    
    @FXML
    public void refreshTable() throws SQLException {
        ObservableList<Resource> newList = DatabaseHelper.getResources();
        tblResource.setItems(newList);
    }
    
}
