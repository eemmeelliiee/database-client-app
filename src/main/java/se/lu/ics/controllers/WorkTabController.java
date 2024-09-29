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
import se.lu.ics.models.Project;
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
        consultantWorkComboBox.setOnAction(event -> {
            String selectedEmpNo = consultantWorkComboBox.getSelectionModel().getSelectedItem();
            if (selectedEmpNo != null) {
                displayTotalWorkedHours(selectedEmpNo);
            }
        });
    }

    // Back to Home Page Button
    @FXML
    private Button backToHomePageButton;

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

    // Add Consultant to Project Pane
    @FXML
    private AnchorPane addConToProjPane;

    @FXML
    private Button showAddConToProjPane;

    @FXML
    private void handleShowAddConToProjPane() {
        addConToProjPane.setVisible(true);
        manageConsultantsForProjectPane.setVisible(false);
        totalWorkHoursPane.setVisible(false);
        otherPane.setVisible(false);
    }

    //Manage consultants for project Pane
    @FXML
    private AnchorPane manageConsultantsForProjectPane;

    @FXML
    private Button manageConsultantsForProject;

    @FXML
    private TableColumn<Consultant, Double> tableColHours;

    @FXML
    private void handleShowManageConsultantsForProjectPane() {
        manageConsultantsForProjectPane.setVisible(true);
        addConToProjPane.setVisible(false);
        totalWorkHoursPane.setVisible(false);
        otherPane.setVisible(false);
        errorLabel.setText("");
        warningLabel.setText("");
    }

    //Total work hours for consultants Pane
    @FXML
    private AnchorPane totalWorkHoursPane;

    @FXML
    private Button showTotalWorkHoursPane;

    @FXML
    private void handleShowTotalWorkHoursPane() {
        totalWorkHoursPane.setVisible(true);
        otherPane.setVisible(false);
        addConToProjPane.setVisible(false);
        manageConsultantsForProjectPane.setVisible(false);
        onAllConsultantsHoursButtonClick();
        errorLabel.setText("");
        warningLabel.setText("");
    }

    //Other Pane
    @FXML
    private AnchorPane otherPane;

    @FXML
    private Button showOtherPane;

    @FXML
    private void handleShowOtherPane() {
        otherPane.setVisible(true);
        addConToProjPane.setVisible(false);
        manageConsultantsForProjectPane.setVisible(false);
        totalWorkHoursPane.setVisible(false);
        displayHardestWorkingConsultant();
        populateTableWithProjectsInvolvingAllConsultants();
        displayConsultantsThreeProjectsOrLess();
        errorLabel.setText("");
        warningLabel.setText("");
    }

    // Add consultant to project
    @FXML
    private ComboBox<String> addConEmpNoCombobox;

    @FXML
    private ComboBox<String> addConProjNoCombobox;

    @FXML
    private TextField addConWorkHours;

    @FXML
    private Button addConButton;

    @FXML
    private Label warningLabel;

    @FXML
    private void handleButtonAddConsultant() {
        try {
            String empNo = addConEmpNoCombobox.getValue(); // Get the selected employee number
            String projNo = addConProjNoCombobox.getValue(); // Get the selected project number
            String workHoursString = addConWorkHours.getText();

            // Convert work hours from String to double
            workHoursString = (workHoursString != null && workHoursString.trim().isEmpty()) ? null : workHoursString;
            double workHours = (workHoursString != null) ? Double.parseDouble(workHoursString) : 0;

            // Save the new Work entry to the database and capture the return value
            String result = workDao.addConsultantToProject(empNo, projNo, workHours);

            // Check if a warning message is returned
            if (result != null) {
                // Status message to inform the user
                errorLabel.setText("Consultant " + empNo + " added to project " + projNo + " successfully");
                errorLabel.setStyle("-fx-text-fill: green");
            } else {
                // Status message to inform the user
                warningLabel.setText("");
                errorLabel.setText("Consultant " + empNo + " added to project " + projNo + " successfully");
                errorLabel.setStyle("-fx-text-fill: green");
            }

            // Clear fields after submission
            addConEmpNoCombobox.setValue(null);
            addConProjNoCombobox.setValue(null);
            addConWorkHours.clear();

        } catch (DaoException e) {
            warningLabel.setText("");
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red");
        } catch (NumberFormatException e) {
            warningLabel.setText("");
            errorLabel.setText("Invalid input for work hours. Please enter a valid number.");
            errorLabel.setStyle("-fx-text-fill: red");
        }
    }

    // Method to populate employee number ComboBox
    @FXML
    private void populateEmployeeNumbers() {
        try {
            // Fetch employee numbers from the ConsultantDao
            List<String> employeeNumbers = consultantDao.findAllEmpNos(); 
            ObservableList<String> employeeNumbersList = FXCollections.observableArrayList(employeeNumbers);
            // Set items in the employee number ComboBox
            addConEmpNoCombobox.setItems(employeeNumbersList); 
            // Set items in the view consultant ComboBox
            consultantWorkComboBox.setItems(employeeNumbersList);
        } catch (DaoException e) {
            errorLabel.setText("Error fetching employee numbers: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red");
        }
    }

    // Consultant working on certain projects
    @FXML
    private ComboBox<String> projectWorkComboBox;

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
    private Label updateHoursResponse;

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
            //Get selected project number from the ComboBox
            String projectNo = projectWorkComboBox.getValue();
            double workHours = 0;
            if (projectNo != null && !projectNo.isEmpty()) {
                try {
                    //Fetch the work hours for the consultant on the selected project
                    workHours = workDao.getWorkHoursForConsultantOnProject(consultant.getEmpNo(), projectNo);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            }
            //Return the work hours wrapped in a SimpleDoubleProperty
            return new SimpleDoubleProperty(workHours).asObject();
        });
    
        //Allow editing of the hours column
        tableColHours.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
    
        //Handle edit commit event to update hours in the database
        tableColHours.setOnEditCommit(event -> {
            Consultant consultant = event.getRowValue();
            String projectNo = projectWorkComboBox.getValue();
            double newWorkHours = event.getNewValue();
    
            //Call the method to update work hours in the database
            updateWorkHours(consultant.getEmpNo(), projectNo, newWorkHours);
            consultantWorkTable.refresh();
        });
    
        // Clear any existing items in the table
        consultantWorkTable.getItems().clear();
    }

    // Method to update work hours in the database
    private void updateWorkHours(String empNo, String projectNo, double newWorkHours) {
        try {
            workDao.updateWorkHours(projectNo, empNo, newWorkHours);
            updateHoursResponse.setText("Hours successfully updated!");
            updateHoursResponse.setStyle("-fx-text-fill: green");
            // Optionally show success message
        } catch (DaoException e) {
            // Handle exception and show error message to the user
            updateHoursResponse.setText(e.getMessage());
            updateHoursResponse.setStyle("-fx-text-fill: red");
        }
    }

    @FXML 
    private Button buttonRemoveConFromProj;

    @FXML
    private void handleButtonRemoveConFromProj() {
        // Get selected consultant from the table
        Consultant selectedConsultant = consultantWorkTable.getSelectionModel().getSelectedItem();

        // Get selected project number from the ComboBox
        String selectedProjectNo = projectWorkComboBox.getValue();

        // Check if both a consultant and a project are selected
        if (selectedConsultant != null && selectedProjectNo != null && !selectedProjectNo.isEmpty()) {
            try {
                // Call the method to delete the consultant from the selected project
                workDao.deleteConsultantFromProject(selectedConsultant.getEmpNo(), selectedProjectNo);

                // Show success message (optional)
                updateHoursResponse.setText("Consultant " + selectedConsultant.getEmpNo() + " removed from project "+ selectedProjectNo +" successfully.");
                updateHoursResponse.setStyle("-fx-text-fill: green");

                // Refresh the consultant work table for the selected project
                updateConsultantWorkTableForProject(selectedProjectNo);
            } catch (DaoException e) {
                // Handle exceptions and show error message
                updateHoursResponse.setText("Error removing consultant: " + e.getMessage());
                updateHoursResponse.setStyle("-fx-text-fill: red");
            }
        } else {
            // If no consultant or project is selected, show an error message
            updateHoursResponse.setText("Please select a consultant and a project.");
            updateHoursResponse.setStyle("-fx-text-fill: red");
        }
    }

    // populate project numbers
    @FXML
    private void populateProjectNumbers() {
        List<String> projectNumbers = projectDao.findAllProjectNos();
        ObservableList<String> projectNumbersList = FXCollections.observableArrayList(projectNumbers);
        projectWorkComboBox.setItems(projectNumbersList);
        addConProjNoCombobox.setItems(projectNumbersList); // Set items in the add consultant ComboBox
    }

    // Set up the ComboBox action listener to update the consultant work table
    @FXML
    private void setProjectComboBoxHandler() {
        projectWorkComboBox.setOnAction(event -> {
            String selectedProjectNo = projectWorkComboBox.getValue();
            if (selectedProjectNo != null && !selectedProjectNo.isEmpty()) {
                updateConsultantWorkTableForProject(selectedProjectNo);
            }
        });
    }

    // Method to update the consultant work table for the selected project
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

    // View total worked hours for a certein consultant
    @FXML
    private ComboBox<String> consultantWorkComboBox;

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

    // Method to handle button click to view total worked hours for all consultants
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

    //View consultants that work on 3 projects or less
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


    //Set up table
    private void setUpConsultantTableThreeProjects() {
        empNoColumn.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        empFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("empFirstName"));
        empLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("empLastName"));
        empTitleColumn.setCellValueFactory(new PropertyValueFactory<>("empTitle"));
        empStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("empStartDate"));

        // Clear any existing items in the table
        consultantsTableThreeProjects.getItems().clear();
    }

    //Method to display consultants who work on three or fewer projects
    @FXML
    private void displayConsultantsThreeProjectsOrLess() {
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

    //Hardest working consultant
    @FXML
    private TableView<Consultant> tableViewHardestWorkingConsultant;
    
    @FXML
    private TableColumn<Consultant, String> tableColHardEmpNo;
    
    @FXML
    private TableColumn<Consultant, String> tableColHardTitle;
    
    @FXML
    private TableColumn<Consultant, String> tableColHardFirstName;
    
    @FXML
    private TableColumn<Consultant, String> tableColHardLastName;
    
    @FXML
    private TableColumn<Consultant, Double> tableColHardTotHours;

    
    @FXML
    public void displayHardestWorkingConsultant() {
        try {
            Consultant consultant = workDao.getHardestWorkingConsultant();  // Get the hardest working consultant

            // Create a list to add this consultant to the TableView
            ObservableList<Consultant> consultantData = FXCollections.observableArrayList();
            consultantData.add(consultant);

            // Bind columns to the Consultant properties
            tableColHardEmpNo.setCellValueFactory(new PropertyValueFactory<>("empNo"));
            tableColHardTitle.setCellValueFactory(new PropertyValueFactory<>("empTitle"));
            tableColHardFirstName.setCellValueFactory(new PropertyValueFactory<>("empFirstName"));
            tableColHardLastName.setCellValueFactory(new PropertyValueFactory<>("empLastName"));

            // Bind the total work hours column to the temporary TotalWorkHours property
            tableColHardTotHours.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalWorkHours()).asObject());

            // Set data in the TableView
            tableViewHardestWorkingConsultant.setItems(consultantData);

        } catch (DaoException e) {
            e.printStackTrace();
            // Handle exception, e.g., show an error dialog
        }
    }

    //Projects involving all consultants
    @FXML
    private TableView<Project> tableViewProjectWithAllCons;

    @FXML
    private TableColumn<Project, String> tableColProjAllConsStatus;

    @FXML
    private TableColumn<Project, String> tableColProjAllConsProjectNo;

    @FXML
    private TableColumn<Project, String> tableColProjAllConsName;

    @FXML
    private TableColumn<Project, LocalDate> tableColProjAllConsStartDate;

    @FXML
    private TableColumn<Project, LocalDate> tableColProjAllConsEndDate;


    private void populateTableWithProjectsInvolvingAllConsultants() {
        // Call the method to get all projects involving all consultants
        List<Project> projects = workDao.getProjectsInvolvingAllConsultants();

        // Map columns to Project fields
        tableColProjAllConsProjectNo.setCellValueFactory(new PropertyValueFactory<>("projectNo"));
        tableColProjAllConsName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        tableColProjAllConsStatus.setCellValueFactory(new PropertyValueFactory<>("projectStatus"));
        tableColProjAllConsStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        tableColProjAllConsEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));

        // Populate the TableView with the data
        ObservableList<Project> projectObservableList = FXCollections.observableArrayList(projects);
        tableViewProjectWithAllCons.setItems(projectObservableList);
    }

    @FXML
    public void onShowProjectsClicked() {
        populateTableWithProjectsInvolvingAllConsultants();
    }
}