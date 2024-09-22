package se.lu.ics.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Handles database connections by reading configuration details from a properties file.
 * This class abstracts the connection logic and allows other classes to obtain a database
 * connection without worrying about the configuration details.
 */
public class ConnectionHandler {
    private String connectionURL;
    private final String propertiedFilePath = "/config/config.properties";

    /**
     * Constructor that initializes the connection URL by loading properties from a file.
     * This method reads database connection properties from a configuration file located in
     * the classpath and constructs the database connection URL.
     *
     * @throws IOException If the configuration file cannot be found or read.
     */
    public ConnectionHandler() throws IOException {

        Properties connectionProperties = new Properties();

        // Load the configuration properties from the file in the classpath
        try (InputStream inputStream = getClass().getResourceAsStream(propertiedFilePath)) {

            // If the file is found, load the properties into the connectionProperties object
            if (inputStream != null) {
                connectionProperties.load(inputStream);
            } else {
                // If the file is not found, throw an exception to inform the caller
                throw new IOException("Config file 'config.properties' not found in classpath");
            }
        }

        // Extract the database connection properties
        String databaseServerName = connectionProperties.getProperty("database.server.name");
        String databaseServerPort = connectionProperties.getProperty("database.server.port");
        String databaseName = connectionProperties.getProperty("database.name");
        String databaseUserName = connectionProperties.getProperty("database.user.name");
        String databaseUserPassword = connectionProperties.getProperty("database.user.password");

        // Construct the JDBC connection URL using the properties
        connectionURL = "jdbc:sqlserver://"
                + databaseServerName + ":"
                + databaseServerPort + ";"
                + "database=" + databaseName + ";"
                + "user=" + databaseUserName + ";"
                + "password=" + databaseUserPassword + ";"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
    }

    /**
     * Establishes and returns a connection to the database using the configured URL.
     *
     * @return A Connection object to the database.
     * @throws SQLException If an error occurs when establishing the connection.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionURL);
    }
}