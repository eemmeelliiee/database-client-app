package se.lu.ics.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import se.lu.ics.App;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
