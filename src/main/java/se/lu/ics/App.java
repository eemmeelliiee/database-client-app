package se.lu.ics;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.MetaDataDao;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.data.WorkDao;
import java.awt.Desktop;
import javafx.scene.layout.AnchorPane;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            String path = "/fxml/MainView.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane root = loader.load();

            Scene primaryScene = new Scene(root);
            primaryStage.setScene(primaryScene);

            primaryStage.setTitle("Home Page");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        /*try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            ConsultantDao consultantDao = new ConsultantDao(connectionHandler);
            ProjectDao projectDao = new ProjectDao(connectionHandler);
            WorkDao workDao = new WorkDao(connectionHandler, consultantDao, projectDao);
            MetaDataDao metaDataDao = new MetaDataDao(connectionHandler);
            MilestoneDao milestoneDao = new MilestoneDao(connectionHandler); 


        } catch (DaoException | IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }*/

        launch(args); // Start the JavaFX application
    }
}
