package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


public class MainViewController {

    // Register Consultant Pane
    @FXML 
    private AnchorPane registerConsultantPane;

    @FXML
    private Button showRegisterConsultantPane;

    @FXML
    private void handleShowRegisterConsultantPane() {
        registerConsultantPane.setVisible(true);
        removeConsultantPane.setVisible(false);
    }

    //Remove consultant Pane
    @FXML
    private AnchorPane removeConsultantPane;

    @FXML
    private Button showRemoveConsultantPane;

    @FXML
    private void handleShowRemoveConsultantPane() {
        removeConsultantPane.setVisible(true);
        registerConsultantPane.setVisible(false);
    }
}
