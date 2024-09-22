package se.lu.ics.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import se.lu.ics.App;
import se.lu.ics.data.ConsultantDao;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    private ConsultantDao consultantDao;

    public PrimaryController() throws Exception { // Maybe doesnt need to throw exception, instead display error message
        try {
            consultantDao = new ConsultantDao();
        } catch (IOException e) {
            throw new Exception("Error initializing database connection: " + e.getMessage());
        }
    }

}
