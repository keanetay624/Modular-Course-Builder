package ModularCourseBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static final String ICON_PATH = "file:src/main/resources/ModularCourseBuilder/icons/modular.png";

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        /**
        * On first app run, setup a database file. If the database has already
        * been setup, do not setup again.
        */    
        Database myDatabase = new Database();
        if (myDatabase.CheckIfAlreadySetUp() == false) {
            myDatabase.setupDatabase();
        }
        
        // launch the login screen
        scene = new Scene(loadFXML("login"), 1280, 720);
        stage.setScene(scene);
        stage.getIcons().add(new Image(ICON_PATH));
        stage.setTitle("Modular Course Builder v2.0");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
}