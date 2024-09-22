package se.lu.ics;

import java.io.File;
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
import se.lu.ics.data.MetaDataDao;  // Import MetaDataDao to test
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import java.awt.Desktop;

import java.io.IOException;
import java.util.List;

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

    // Used for opening the xlsx file. Should probably be moved to a controller
    private static void openExcelFile(String filePath) {
        try {
            File file = new File(filePath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file); // Open the file with the default application
            } else {
                System.err.println("Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            System.err.println("Error opening Excel file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            // Instantiate ConsultantDao
            ConsultantDao consultantDao = new ConsultantDao();
            ConnectionHandler connectionHandler = consultantDao.getConnectionHandler();
    
            // Instantiate MetaDataDao and test the getNonIntegerColumns method
            MetaDataDao metaDataDao = new MetaDataDao();
    
            // Call the method to get non-integer columns (no need to print)
            List<String> nonIntegerColumns = metaDataDao.getNonIntegerColumns();
    
            // Test the getTableWithMostRows method (no need to print)
            String[] tableInfo = metaDataDao.getTableWithMostRows();

            openExcelFile("src\\main\\resources\\excel\\ExcelData.xlsx");
    
        } catch (DaoException | IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    
        launch();  // Start the JavaFX application
    }
}
