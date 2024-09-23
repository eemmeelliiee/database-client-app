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


    //Project Tab 

    //Register Project Pane
    @FXML 
    private AnchorPane registerProjectPane;

    @FXML
    private Button showRegisterProjectPane;

    @FXML
    private void handleShowRegisterProjectPane() {
        registerProjectPane.setVisible(true);
        removeProjectPane.setVisible(false);
        updateProjectPane.setVisible(false);
        infoProjectPane.setVisible(false);
    }

    //Remove Project Pane
    @FXML
    private AnchorPane removeProjectPane;

    @FXML
    private Button showRemoveProjectPane;

    @FXML
    private void handleShowRemoveProjectPane() {
        removeProjectPane.setVisible(true);
        registerProjectPane.setVisible(false);
        updateProjectPane.setVisible(false);
        infoProjectPane.setVisible(false);
    }

    // Update Project Pane
    @FXML
    private AnchorPane updateProjectPane;

    @FXML
    private Button showUpdateProjectPane;

    @FXML
    private void handleShowUpdateProjectPane() {
        updateProjectPane.setVisible(true);
        registerProjectPane.setVisible(false);
        removeProjectPane.setVisible(false);
        infoProjectPane.setVisible(false);
    
    }

    // info pane
    @FXML
    private AnchorPane infoProjectPane;

    @FXML
    private Button showInfoProjectPane;

    @FXML
    private void handleShowInfoProjectPane() {
        infoProjectPane.setVisible(true);
        registerProjectPane.setVisible(false);
        removeProjectPane.setVisible(false);
        updateProjectPane.setVisible(false);
    }

    //Milestone Tab

    //Register Milestone Pane
    @FXML
    private AnchorPane registerMSPane;

    @FXML
    private Button showRegisterMSPane;

    @FXML
    private void handleShowRegisterMSPane() {
        registerMSPane.setVisible(true);
        removeMSPane.setVisible(false);
        addMSPane.setVisible(false);
        retrieveMSPane.setVisible(false);
    }

    //Remove Milestone Pane
    @FXML
    private AnchorPane removeMSPane;

    @FXML
    private Button showRemoveMSPane;

    @FXML
    private void handleShowRemoveMSPane() {
        removeMSPane.setVisible(true);
        registerMSPane.setVisible(false);
        addMSPane.setVisible(false);
        retrieveMSPane.setVisible(false);
    }

    // Update Milestone Pane
    @FXML
    private AnchorPane addMSPane;

    @FXML
    private Button showAddMSPane;

    @FXML
    private void handleShowAddMSPane() {
        addMSPane.setVisible(true);
        registerMSPane.setVisible(false);
        removeMSPane.setVisible(false);
        retrieveMSPane.setVisible(false);
    
    }

    // retrieve pane
    @FXML
    private AnchorPane retrieveMSPane;

    @FXML
    private Button showRetrieveMSPane;

    @FXML
    private void handleShowRetrieveMSPane() {
        retrieveMSPane.setVisible(true);
        registerMSPane.setVisible(false);
        removeMSPane.setVisible(false);
        addMSPane.setVisible(false);
    }

    //Work Tab

    //Add Consultant to Project Pane
    @FXML
    private AnchorPane addConToProPane;

    @FXML
    private Button showAddConToProPane;

    @FXML
    private void handleShowAddConToProPane() {
        addConToProPane.setVisible(true);
        checkWorkingConsPane.setVisible(false);
        updateHoursPane.setVisible(false);
        displayConsHoursPane.setVisible(false);
        hardestWorkingConPane.setVisible(false);
    }

    //Check Working Consultants Pane
    @FXML
    private AnchorPane checkWorkingConsPane;

    @FXML
    private Button showCheckWorkingConsPane;

    @FXML
    private void handleShowCheckWorkingConsPane() {
        checkWorkingConsPane.setVisible(true);
        addConToProPane.setVisible(false);
        updateHoursPane.setVisible(false);
        displayConsHoursPane.setVisible(false);
        hardestWorkingConPane.setVisible(false);
    }

    // Update Hours Pane
    @FXML
    private AnchorPane updateHoursPane;

    @FXML
    private Button showUpdateHoursPane;

    @FXML
    private void handleShowUpdateHoursPane() {
        updateHoursPane.setVisible(true);
        addConToProPane.setVisible(false);
        checkWorkingConsPane.setVisible(false);
        displayConsHoursPane.setVisible(false);
        hardestWorkingConPane.setVisible(false);
    }

    // Display Consultant Hours Pane
    @FXML
    private AnchorPane displayConsHoursPane;

    @FXML
    private Button showDisplayConsHoursPane;

    @FXML
    private void handleShowDisplayConsHoursPane() {
        displayConsHoursPane.setVisible(true);
        addConToProPane.setVisible(false);
        checkWorkingConsPane.setVisible(false);
        updateHoursPane.setVisible(false);
        hardestWorkingConPane.setVisible(false);
    }

    // Hardest Working Consultant Pane
    @FXML
    private AnchorPane hardestWorkingConPane;

    @FXML
    private Button showHardestWorkingConPane;

    @FXML
    private void handleShowHardestWorkingConPane() {
        hardestWorkingConPane.setVisible(true);
        addConToProPane.setVisible(false);
        checkWorkingConsPane.setVisible(false);
        updateHoursPane.setVisible(false);
        displayConsHoursPane.setVisible(false);
    }

    //MetaData Tab
}
