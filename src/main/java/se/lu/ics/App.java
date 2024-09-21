package se.lu.ics;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public static void main(String[] args) {
        connectToDatabaseAndQuery();
        launch();
    }

    private static void connectToDatabaseAndQuery() {
        Properties connectionProperties = new Properties();

        try {
            FileInputStream stream = new FileInputStream("src/main/resources/config/test.config.properties");
            connectionProperties.load(stream);

            // Extract the database connection properties
            String databaseServerName = connectionProperties.getProperty("database.server.name");
            String databaseServerPort = connectionProperties.getProperty("database.server.port");
            String databaseName = connectionProperties.getProperty("database.name");
            String databaseUserName = connectionProperties.getProperty("database.user.name");
            String databaseUserPassword = connectionProperties.getProperty("database.user.password");

            // Construct the JDBC connection URL using the properties
            String connectionURL = "jdbc:sqlserver://"
                    + databaseServerName + ":"
                    + databaseServerPort + ";"
                    + "database=" + databaseName + ";"
                    + "user=" + databaseUserName + ";"
                    + "password=" + databaseUserPassword + ";"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

            String query = "SELECT * FROM Patient";

            Connection connection = DriverManager.getConnection(connectionURL);

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Patient id: " + resultSet.getString("PatientId"));
                System.out.println("Patient name: " + resultSet.getString("PatientName"));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not load properties file");
            System.exit(1);
        }
    }

}