package se.lu.ics.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import se.lu.ics.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}