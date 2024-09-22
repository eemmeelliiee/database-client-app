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
import se.lu.ics.models.Consultant;

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
        // connectToDatabaseAndQuery();
        // launch();

        try {
            ConsultantDao consultantDao = new ConsultantDao();

            // 1. Test retrieving all consultants
            System.out.println("Retrieving all consultants:");
            List<Consultant> allConsultants = consultantDao.findAll();
            for (Consultant consultant : allConsultants) {
                System.out.println(consultant);
            }

            // 2. Test saving a new consultant
            System.out.println("\nSaving a new consultant:");
            Consultant newConsultant = new Consultant("EMP1001", "John", "Doe", "Senior Consultant",
                    LocalDate.of(2020, 1, 15));
            consultantDao.save(newConsultant);
            System.out.println("Consultant saved: " + newConsultant);

            // 3. Test retrieving a consultant by EmpNo
            System.out.println("\nRetrieving consultant by EmpNo:");
            Consultant consultantByEmpNo = consultantDao.findByEmpNo("EMP1001");
            System.out.println("Consultant found: " + consultantByEmpNo);

            // 4. Test updating a consultant
            System.out.println("\nUpdating consultant:");
            consultantByEmpNo.setEmpLastName("Smith");
            consultantDao.update(consultantByEmpNo);
            System.out.println("Consultant updated: " + consultantByEmpNo);

            // 5. Test retrieving consultants by title
            System.out.println("\nRetrieving consultants by title 'Senior Consultant':");
            List<Consultant> consultantsByTitle = consultantDao.findByEmpTitle("Senior IT");
            for (Consultant consultant : consultantsByTitle) {
                System.out.println(consultant);
            }

            // 6. Test deleting a consultant by EmpNo
            System.out.println("\nDeleting consultant by EmpNo:");
            consultantDao.deleteByEmpNo("EMP1001");
            System.out.println("Consultant with EmpNo 'EMP1001' deleted.");

            // 7. Test retrieving the total number of consultants
            System.out.println("\nRetrieving total number of consultants:");
            int totalConsultants = consultantDao.getTotalNumberOfConsultants();
            System.out.println("Total number of consultants: " + totalConsultants);

        } catch (IOException e) {
            System.err.println("Error initializing ConsultantDao: " + e.getMessage());
        } catch (DaoException e) {
            System.err.println("DAO Error: " + e.getMessage());
        }

        launch();

    }

    // private static void connectToDatabaseAndQuery() {
    //     Properties connectionProperties = new Properties();

    //     try {
    //         FileInputStream stream = new FileInputStream("src/main/resources/config/test.config.properties");
    //         connectionProperties.load(stream);

    //         // Extract the database connection properties
    //         String databaseServerName = connectionProperties.getProperty("database.server.name");
    //         String databaseServerPort = connectionProperties.getProperty("database.server.port");
    //         String databaseName = connectionProperties.getProperty("database.name");
    //         String databaseUserName = connectionProperties.getProperty("database.user.name");
    //         String databaseUserPassword = connectionProperties.getProperty("database.user.password");

    //         // Construct the JDBC connection URL using the properties
    //         String connectionURL = "jdbc:sqlserver://"
    //                 + databaseServerName + ":"
    //                 + databaseServerPort + ";"
    //                 + "database=" + databaseName + ";"
    //                 + "user=" + databaseUserName + ";"
    //                 + "password=" + databaseUserPassword + ";"
    //                 + "encrypt=true;"
    //                 + "trustServerCertificate=true;";

    //         String query = "SELECT * FROM Consultant";

    //         Connection connection = DriverManager.getConnection(connectionURL);

    //         PreparedStatement preparedStatement = connection.prepareStatement(query);

    //         ResultSet resultSet = preparedStatement.executeQuery();

    //         while (resultSet.next()) {
    //             System.out.println("Patient id: " + resultSet.getString("EmpNo"));
    //             System.out.println("Patient name: " + resultSet.getString("EmpFirstName"));
    //         }

    //         resultSet.close();
    //         preparedStatement.close();
    //         connection.close();

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Could not load properties file");
    //         System.exit(1);
    //     }
    // }

}