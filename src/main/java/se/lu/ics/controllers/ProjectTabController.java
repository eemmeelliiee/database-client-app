package se.lu.ics.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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


import se.lu.ics.models.Project;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.ConnectionHandler;

public class ProjectTabController {

    //Dao instance
    private ProjectDao projectDao;

    // Constructor
    public ProjectTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            projectDao = new ProjectDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        populateProjectNumbers();
    }

    // Back to Home Page Button
    @FXML
    private Button backToHomePageButton;

    //Button to get to the project tab
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

    // Project Tab

    // Register Project Pane
    @FXML
    private AnchorPane registerProjectPane;

    @FXML
    private Button showRegisterProjectPane;

    @FXML
    private void handleShowRegisterProjectPane() {
        registerProjectPane.setVisible(true);
        removeProjectPane.setVisible(false);
        infoProjectPane.setVisible(false);
    }

    // Remove Project Pane
    @FXML
    private AnchorPane removeProjectPane;

    @FXML
    private Button showRemoveProjectPane;

    @FXML
    private void handleShowRemoveProjectPane() {
        removeProjectPane.setVisible(true);
        registerProjectPane.setVisible(false);
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
    }

    // Register Project
    @FXML
    private TextField registerProjectNo;

    @FXML
    private TextField registerProjectName;

    @FXML
    private DatePicker registerProjectStartDate;

    @FXML
    private DatePicker registerProjectEndDate;

    @FXML
    private Button registerProjectButton;

    @FXML
    private Label registerProjectLabelResponse;

    @FXML
    private void handleButtonRegisterProject() {
        try {
            String projectNo = registerProjectNo.getText();
            String projectName = registerProjectName.getText();
            LocalDate projectStartDate = registerProjectStartDate.getValue();
            LocalDate projectEndDate = registerProjectEndDate.getValue();

            // Set to null if the text is empty
            projectNo = (projectNo != null && projectNo.trim().isEmpty()) ? null : projectNo;
            projectName = (projectName != null && projectName.trim().isEmpty()) ? null : projectName;

            Project newProject = new Project(projectNo, projectName, projectStartDate, projectEndDate);

            projectDao.save(newProject);

            registerProjectLabelResponse.setText(
                    "New Project Registered:\n" +
                            "Project Name: " + (projectName != null ? projectName : "N/A") + "\n" +
                            "Project No: " + (projectNo != null ? projectNo : "N/A") + "\n" +
                            "Start Date: " + (projectStartDate != null ? projectStartDate : "N/A") + "\n" +
                            "End Date: " + (projectEndDate != null ? projectEndDate : "N/A"));

            populateProjectNumbers();
        } catch (DaoException e) {
            registerProjectLabelResponse.setText(e.getMessage());

        }
    }

    // Remove Project
    @FXML
    private ComboBox<String> removeProjectNo;

    @FXML
    private Button removeProjectButton;

    @FXML
    private Label removeProjectLabelResponse;

    @FXML
    private void handleButtonRemoveProject() {
        String projectNo = removeProjectNo.getValue(); // Get the selected Project Number

        // Check if a Project Number is selected
        if (projectNo != null && !projectNo.isEmpty()) {
            projectDao.deleteByProjectNo(projectNo); // Remove the project

            // Create a response message with a newline for formatting
            String responseMessage = String
                    .format("Project with Project No: " + projectNo + " has been successfully removed.");

            // Set the formatted string to the response label
            removeProjectLabelResponse.setText(responseMessage);

            // Optional: Refresh the ComboBox after removal
            populateProjectNumbers(); // Assuming you have a method to refresh the ComboBox
        } else {
            // If no Project Number is selected, show a warning message
            removeProjectLabelResponse.setText("Please select a project number.");
        }
    }

    // Populate Project Numbers
    @FXML
    private void initializeProject() {
        populateProjectNumbers();
    }

    @FXML
    private void populateProjectNumbers() {
        List<String> projectNumbers = projectDao.findAllProjectNos();
        removeProjectNo.getItems().clear();
        removeProjectNo.getItems().addAll(projectNumbers);
        // infoProjectNo.getItems().clear();
        // infoProjectNo.getItems().addAll(projectNumbers);
    }

}
