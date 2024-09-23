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
        updateConsultantPane.setVisible(false);
        infoPane.setVisible(false);
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
        updateConsultantPane.setVisible(false);
        infoPane.setVisible(false);
    }

    // Update Consultant Pane
    @FXML
    private AnchorPane updateConsultantPane;

    @FXML
    private Button showUpdateConsultantPane;

    @FXML
    private void handleShowUpdateConsultantPane() {
        updateConsultantPane.setVisible(true);
        registerConsultantPane.setVisible(false);
        removeConsultantPane.setVisible(false);
        infoPane.setVisible(false);
    
    }

    // info pane
    @FXML
    private AnchorPane infoPane;

    @FXML
    private Button showInfoPane;

    @FXML
    private void handleShowInfoPane() {
        infoPane.setVisible(true);
        registerConsultantPane.setVisible(false);
        removeConsultantPane.setVisible(false);
        updateConsultantPane.setVisible(false);
    }



}
