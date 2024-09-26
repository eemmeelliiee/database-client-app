package se.lu.ics.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.ComboBox;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Work;
import se.lu.ics.data.WorkDao;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ConnectionHandler;

public class WorkTabController {

    // Dao instance
    private WorkDao workDao;
    private ConsultantDao consultantDao;
    private ProjectDao projectDao;

    // Constructor
    public WorkTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            consultantDao = new ConsultantDao(connectionHandler);
            projectDao = new ProjectDao(connectionHandler);

            workDao = new WorkDao(connectionHandler, consultantDao, projectDao);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        setUpConsultantTable();
        populateProjectNumbers();
        populateEmployeeNumbers();
        setProjectComboBoxHandler();
        setUpConsultantTableThreeProjects();
        onAllConsultantsHoursButtonClick();

        // Set up the ComboBox action listener to display total worked hours
        consultantComboBox.setOnAction(event -> {
            String selectedEmpNo = consultantComboBox.getSelectionModel().getSelectedItem();
            if (selectedEmpNo != null) {
                displayTotalWorkedHours(selectedEmpNo);
            }
        });
    }

    // Back to Home Page Button
    @FXML
    private Button backToHomePageButton;

    // Button to get to the project tab
    @FXML
    private void handleBackToHomePageButton(ActionEvent event) {
        String path = "/fxml/MainView.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            AnchorPane root = loader.load();
            Stage primaryStage = new Stage();
            Scene primaryScene = new Scene(root);

            primaryStage.setScene(primaryScene);

            primaryStage.setTitle("Home Page");
            primaryStage.show();

            // Close the current stage
            Stage currentStage = (Stage) backToHomePageButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Work Tab

    // Add Consultant to Project Pane
    @FXML
    private AnchorPane addConToProPane;

    @FXML
    private Button showAddConToProPane;

    @FXML
    private void handleShowAddConToProPane() {
        addConToProPane.setVisible(true);
        checkWorkingConsPane.setVisible(false);
        displayConsHoursPane.setVisible(false);
        hardestWorkingConPane.setVisible(false);
    }

    // Check Working Consultants Pane
    @FXML
    private AnchorPane checkWorkingConsPane;

    @FXML
    private Button manageConsultantsForProject;

    @FXML
    private TableColumn<Consultant, Double> tableColHours;

    @FXML
    private void handleManageConsultantsForProject() {
        checkWorkingConsPane.setVisible(true);
        addConToProPane.setVisible(false);
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
        hardestWorkingConPane.setVisible(false);
        onAllConsultantsHoursButtonClick();

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
        displayConsHoursPane.setVisible(false);
    }

    // Projects involving all consultants
    @FXML
    Button projectsInvolvingAllConsultants;

    // private void handleButtonProjectsInvolvingAllConsultants(){

    // }

    // Add consultant to project
    @FXML
    private ComboBox<String> addConEmpNo;

    @FXML
    private ComboBox<String> addConProjNo;

    @FXML
    private TextField addConWorkHours;

    @FXML
    private Button addConButton;

    @FXML
    private void handleButtonAddConsultant() {
        try {
            String empNo = addConEmpNo.getValue(); // Get the selected employee number
            String projNo = addConProjNo.getValue(); // Get the selected project number
            String workHoursString = addConWorkHours.getText();

            // Convert work hours from String to double
            workHoursString = (workHoursString != null && workHoursString.trim().isEmpty()) ? null : workHoursString;
            double workHours = (workHoursString != null) ? Double.parseDouble(workHoursString) : 0;

            // Save the new Work entry to the database
            workDao.addConsultantToProject(empNo, projNo, workHours);

            // Status message to inform the user
            errorLabel.setText(
                    "Consultant " + empNo + " added to project " + projNo + " successfully!" + "\n" +
                            "Work Hours: " + (workHoursString != null ? workHoursString : "0.00") + "\n");
            errorLabel.setStyle("-fx-text-fill: green");

            // Clear fields after submission
            addConEmpNo.setValue(null);
            addConProjNo.setValue(null);
            addConWorkHours.clear();

        } catch (DaoException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red");
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid input for work hours. Please enter a valid number.");
            errorLabel.setStyle("-fx-text-fill: red");
            /*
             * } catch (Exception e) {
             * errorLabel.setText("An unexpected error occurred.");
             * e.printStackTrace();
             */
        }
    }

    // Method to populate employee number ComboBox
    @FXML
    private void populateEmployeeNumbers() {
        try {
            List<String> employeeNumbers = consultantDao.findAllEmpNos(); // Fetch employee numbers
            ObservableList<String> employeeNumbersList = FXCollections.observableArrayList(employeeNumbers);
            addConEmpNo.setItems(employeeNumbersList); // Set items in the employee number ComboBox
            consultantComboBox.setItems(employeeNumbersList); // Set items in the view consultant ComboBox
        } catch (DaoException e) {
            errorLabel.setText("Error fetching employee numbers: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red");
        }
    }

    // Consultant working on certain projects
    @FXML
    private ComboBox<String> projectComboBox;

    @FXML
    private TableView<Consultant> consultantWorkTable;

    @FXML
    private TableColumn<Consultant, String> consultantNumberColumn;

    @FXML
    private TableColumn<Consultant, String> consultantFirstNameColumn;

    @FXML
    private TableColumn<Consultant, String> consultantLastNameColumn;

    @FXML
    private TableColumn<Consultant, String> consultantTitleColumn;

    @FXML
    private TableColumn<Consultant, String> consultantDateColumn;

    @FXML
    private Label errorLabel;

    @FXML
    private Label labelUpdateHours;

    // set up the consultant table
    private void setUpConsultantTable() {
        consultantWorkTable.setEditable(true);

        // Set up the columns for the consultant table
        consultantNumberColumn.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        consultantFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("empFirstName"));
        consultantLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("empLastName"));
        consultantTitleColumn.setCellValueFactory(new PropertyValueFactory<>("empTitle"));
        consultantDateColumn.setCellValueFactory(new PropertyValueFactory<>("empStartDate"));
    
        // Custom cell factory for the hours column
        tableColHours.setCellValueFactory(cellData -> {
            Consultant consultant = cellData.getValue();
            String projectNo = projectComboBox.getValue(); // Get selected project number from the ComboBox
            double workHours = 0;
            if (projectNo != null && !projectNo.isEmpty()) {
                try {
                    // Fetch the work hours for the consultant on the selected project
                    workHours = workDao.getWorkHoursForConsultantOnProject(consultant.getEmpNo(), projectNo);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            }
            // Return the work hours wrapped in a SimpleDoubleProperty
            return new SimpleDoubleProperty(workHours).asObject();
        });
    
        // Allow editing of the hours column
        tableColHours.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
    
        // Handle edit commit event to update hours in the database
        tableColHours.setOnEditCommit(event -> {
            Consultant consultant = event.getRowValue();
            String projectNo = projectComboBox.getValue(); // Get the selected project number
            double newWorkHours = event.getNewValue(); // Get the new work hours value
    
            // Call the method to update work hours in the database
            updateWorkHours(consultant.getEmpNo(), projectNo, newWorkHours);
            
            // Optional: Refresh the table to show updated hours
            consultantWorkTable.refresh();
        });
    
        // Clear any existing items in the table
        consultantWorkTable.getItems().clear();
    }

    private void updateWorkHours(String empNo, String projectNo, double newWorkHours) {
        try {
            workDao.updateWorkHours(projectNo, empNo, newWorkHours);
            labelUpdateHours.setText("Hours successfully updated!");
            labelUpdateHours.setStyle("-fx-text-fill: green");
            // Optionally show success message
        } catch (DaoException e) {
            // Handle exception and show error message to the user
            labelUpdateHours.setText(e.getMessage());
            labelUpdateHours.setStyle("-fx-text-fill: red");
        }
    }

    @FXML 
    private Button buttonRemoveConFromProj;

    @FXML
private void handleButtonRemoveConFromProj() {
    // Get selected consultant from the table
    Consultant selectedConsultant = consultantWorkTable.getSelectionModel().getSelectedItem();

    // Get selected project number from the ComboBox
    String selectedProjectNo = projectComboBox.getValue();

    // Check if both a consultant and a project are selected
    if (selectedConsultant != null && selectedProjectNo != null && !selectedProjectNo.isEmpty()) {
        try {
            // Call the method to delete the consultant from the selected project
            workDao.deleteConsultantFromProject(selectedConsultant.getEmpNo(), selectedProjectNo);

            // Show success message (optional)
            labelUpdateHours.setText("Consultant " + selectedConsultant.getEmpNo() + " removed from project "+ selectedProjectNo +" successfully.");
            labelUpdateHours.setStyle("-fx-text-fill: green");

            // Refresh the consultant work table for the selected project
            updateConsultantWorkTableForProject(selectedProjectNo);
        } catch (DaoException e) {
            // Handle exceptions and show error message
            labelUpdateHours.setText("Error removing consultant: " + e.getMessage());
            labelUpdateHours.setStyle("-fx-text-fill: red");
        }
    } else {
        // If no consultant or project is selected, show an error message
        labelUpdateHours.setText("Please select a consultant and a project.");
        labelUpdateHours.setStyle("-fx-text-fill: red");
    }
}


    

    // populate project numbers
    @FXML
    private void populateProjectNumbers() {
        List<String> projectNumbers = projectDao.findAllProjectNos();
        ObservableList<String> projectNumbersList = FXCollections.observableArrayList(projectNumbers);
        projectComboBox.setItems(projectNumbersList);
        addConProjNo.setItems(projectNumbersList); // Set items in the add consultant ComboBox
    }

    @FXML
    private void setProjectComboBoxHandler() {
        projectComboBox.setOnAction(event -> {
            String selectedProjectNo = projectComboBox.getValue();
            if (selectedProjectNo != null && !selectedProjectNo.isEmpty()) {
                updateConsultantWorkTableForProject(selectedProjectNo);
            }
        });
    }

    private void updateConsultantWorkTableForProject(String projectNo) {
        try {
            // Fetch consultants working on the selected project from the DAO
            List<Consultant> consultants = workDao.listConsultantsByProject(projectNo);

            if (consultants != null && !consultants.isEmpty()) {
                ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants);
                consultantWorkTable.setItems(consultantList);
            } else {
                // Clear the table if no consultants are found for the selected project
                consultantWorkTable.getItems().clear();
            }
        } catch (DaoException e) {
            // Handle any database exceptions
            System.err.println("Error fetching consultants for project: " + e.getMessage());
        }
    }

    // View Consultant work hours

    // View total worked hours for a certein consultant
    @FXML
    private ComboBox<String> consultantComboBox;

    @FXML
    private Label totalWorkedHoursLabel;

    @FXML
    private Label totHoursWorkedForCon;

    // Display total worked hours for the selected consultant
    private void displayTotalWorkedHours(String empNo) {
        try {
            double totalHours = workDao.getTotalWorkHoursForConsultant(empNo); // Fetch total hours
            totHoursWorkedForCon.setText("" + totalHours); // Display in label
        } catch (DaoException e) {
            System.err.println("Error retrieving total worked hours: " + e.getMessage());
            totHoursWorkedForCon.setText("Error retrieving hours");
        }
    }

    // Method to handle button click

    private void onAllConsultantsHoursButtonClick() {
        try {
            // Get total worked hours from WorkDao
            double totalHours = workDao.getTotalHoursWorked();

            // Update label to display total worked hours
            totalWorkedHoursLabel.setText("" + totalHours);
        } catch (DaoException e) {
            // In case of error, you can show an error message on the label
            totalWorkedHoursLabel.setText("Error fetching total hours: " + e.getMessage());
        }
    }

    // view consultants that work on 3 projects or less
    @FXML
    private TableView<Consultant> consultantsTableThreeProjects;

    @FXML
    private TableColumn<Consultant, String> empNoColumn;

    @FXML
    private TableColumn<Consultant, String> empFirstNameColumn;

    @FXML
    private TableColumn<Consultant, String> empLastNameColumn;

    @FXML
    private TableColumn<Consultant, String> empTitleColumn;

    @FXML
    private TableColumn<Consultant, String> empStartDateColumn;

    @FXML
    private Button showConsultantsButton;

    // set up table
    private void setUpConsultantTableThreeProjects() {
        empNoColumn.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        empFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("empFirstName"));
        empLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("empLastName"));
        empTitleColumn.setCellValueFactory(new PropertyValueFactory<>("empTitle"));
        empStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("empStartDate"));

        consultantsTableThreeProjects.getItems().clear();
    }

    // set up button
    // Method to handle button click
    @FXML
    private void onShowConsultantsButtonClick() {
        try {
            // Get consultants who work on three or fewer projects from WorkDao
            List<Consultant> consultants = workDao.getConsultantsWithThreeProjectsOrLess();

            // Convert list to ObservableList for TableView
            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants);

            // Set the items for the TableView
            consultantsTableThreeProjects.setItems(consultantList);
        } catch (DaoException e) {
            // Handle any errors, you can log them or display an error message in the UI
            System.out.println("Error fetching consultants: " + e.getMessage());
        }
    }

}
