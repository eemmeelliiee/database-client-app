package se.lu.ics;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {

        try {
            ConsultantDao consultantDao = new ConsultantDao();
            ConnectionHandler connectionHandler = consultantDao.getConnectionHandler();

            // Here goes testing code
            // Test 1:

        } catch (DaoException | IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
        launch();

    }


}