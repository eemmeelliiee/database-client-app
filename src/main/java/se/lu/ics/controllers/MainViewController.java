package se.lu.ics.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ConnectionHandler;


public class MainViewController {

    //DAO instance
   // private ConsultantDao consultantDao = new ConsultantDao();

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

    //Register Consultant
    @FXML
    private TextField registerConsultantNo;

    @FXML
    private TextField registerConsultantName;

    @FXML
    private TextField registerConsultantLast;

    @FXML
    private TextField registerConsultantTitle;

    @FXML
    private DatePicker registerConsultantDate;

    @FXML
    private Button registerConsultantButton;

    @FXML
    private Label lableResponse;

    //DAO instance
    private ConsultantDao consultantDao;

    //Constructor
    public MainViewController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            consultantDao = new ConsultantDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonRegisterConsultant() {
        String empNo = registerConsultantNo.getText();
        String empFirstName = registerConsultantName.getText();
        String empLastName = registerConsultantLast.getText();
        String empTitle = registerConsultantTitle.getText();
        LocalDate empStartDate = registerConsultantDate.getValue();

        Consultant newConsultant = new Consultant(empNo, empFirstName, empLastName, empTitle, empStartDate);

        consultantDao.save(newConsultant);

        lableResponse.setText("New Consultant Registered:\n" +
                      "First Name: " + empFirstName + "\n" +
                      "Last Name: " + empLastName + "\n" +
                      "Title: " + empTitle + "\n" +
                      "Employee No: " + empNo + "\n" +
                      "Start Date: " + empStartDate);

        populateEmployeeNumbers();

        
    }

    //Remove Consultant
    @FXML
    private ComboBox<String> removeConsultantNo; // combobox for employee number

    @FXML
    private Button removeConsultantButton;

    @FXML
    private Label removeConsultantResponse;

    @FXML
    private void initialize() {
        populateEmployeeNumbers();
    }

    private void populateEmployeeNumbers() {
        List<String> employeeNumbers = consultantDao.findAllEmpNos();
        removeConsultantNo.getItems().clear();
        removeConsultantNo.getItems().addAll(employeeNumbers);
    }

    @FXML
    private void handleButtonRemoveConsultant() {
        String empNo = removeConsultantNo.getValue(); // Get the selected Employee Number
        
        // Check if an Employee Number is selected
        if (empNo != null && !empNo.isEmpty()) {
            consultantDao.deleteByEmpNo(empNo); // Remove the consultant
            
            // Create a response message with a newline for formatting
            String responseMessage = String.format("Consultant with Employee No: " + empNo +  " Successfully removed.");
            
            // Set the formatted string to the response label
            removeConsultantResponse.setText(responseMessage);
            
            // Optional: Refresh the ComboBox after removal
            populateEmployeeNumbers(); // Assuming you have a method to refresh the ComboBox
        } else {
            // If no Employee Number is selected, show a warning message
            removeConsultantResponse.setText("Please select an employee number.");
        }
    }
    



}
