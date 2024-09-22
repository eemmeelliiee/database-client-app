package se.lu.ics.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import se.lu.ics.App;
import se.lu.ics.data.ConnectionHandler;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    private ConnectionHandler connectionHandler;

    public PrimaryController() throws Exception { // Maybe doesnt need to throw exception, instead display error message
        try {
            connectionHandler = new ConnectionHandler();
        } catch (IOException e) {
            throw new Exception("Error initializing database connection: " + e.getMessage());
        }
    }

}
