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
import javafx.scene.control.SplitPane;
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

    // Dao instance
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
        populateViewAllProjectsComboBox();
        setViewSpecificProjectsComboBoxHandler();
        setupTableColumns();
        setUpTableView();
        handleButtonViewAllProjects();
    }

    //Back to Home Page Button
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

    //Button to get to the milestone page
    @FXML
    private Button showMilestonePageButton;
    
    @FXML
    private void handleShowMilestonePageButton(ActionEvent event) {
        String path = "/fxml/MilestoneTab.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            SplitPane root = loader.load();
            Stage milestoneStage = new Stage();
            Scene milestoneScene = new Scene(root);
            
            milestoneStage.setScene(milestoneScene);

            milestoneStage.setTitle("Milestone");
            milestoneStage.show();

            // Close the current stage
            Stage currentStage = (Stage) showMilestonePageButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Manage projects pane
    @FXML
    private AnchorPane manageProjectsPane;

    @FXML
    private Button showManageProjectsPane;

    @FXML
    private void handleShowManageProjectsPane() {
        manageProjectsPane.setVisible(true);
        registerProjectPane.setVisible(false);
        handleButtonViewAllProjects();
        registerProjectLabelResponse.setText("");
    }

    // Register Project Pane
    @FXML
    private AnchorPane registerProjectPane;

    @FXML
    private Button showRegisterProjectPane;

    @FXML
    private void handleShowRegisterProjectPane() {
        registerProjectPane.setVisible(true);
        manageProjectsPane.setVisible(false);
    }

    // Remove Project from Company

    @FXML
    private Button buttonRemoveProjFromCompany;

    @FXML
    private Label manageProjectsResponse;

    @FXML
    private void handleButtonRemoveProjFromCompany() {
        // Get the selected project from the table
        Project selectedProject = tableViewManageProject.getSelectionModel().getSelectedItem();

        // Check if a project is selected
        if (selectedProject != null) {
            String projectNo = selectedProject.getProjectNo();

            try {
                // Call DAO method to delete project from the database
                projectDao.deleteByProjectNo(projectNo);

                // Show success message
                manageProjectsResponse.setText("Project " + projectNo + " has been successfully removed.");
                manageProjectsResponse.setStyle("-fx-text-fill: green");

                // Refresh the table view to remove the deleted project
                handleButtonViewAllProjects();

                // Optional: Refresh the combo boxes for projects
                // populateProjectNumbers();
                populateViewAllProjectsComboBox();

            } catch (DaoException e) {
                // Handle exceptions and show error message
                manageProjectsResponse.setText("Error removing project (ProjectNo: " + projectNo + "): " + e.getMessage());
                manageProjectsResponse.setStyle("-fx-text-fill: red");
            }
        } else {
            // No project selected, show error message
            manageProjectsResponse.setText("Please select a project to remove.");
            manageProjectsResponse.setStyle("-fx-text-fill: red");
        }
    }

    // Show All Projects
    @FXML
    private ComboBox<String> viewSpecificProjectComboBox;

    @FXML
    private TableView<Project> tableViewManageProject;

    @FXML
    private TableColumn<Project, String> colStatus;

    @FXML
    private TableColumn<Project, String> colProjectNo;

    @FXML
    private TableColumn<Project, String> colProjectName;

    @FXML
    private TableColumn<Project, LocalDate> colStartDate;

    @FXML
    private TableColumn<Project, LocalDate> colEndDate;

    private void populateViewAllProjectsComboBox() {
        List<String> projectNumbers = projectDao.findAllProjectNos();
        viewSpecificProjectComboBox.getItems().clear();
        viewSpecificProjectComboBox.getItems().addAll(projectNumbers);
    }

    private void setViewSpecificProjectsComboBoxHandler() {
        viewSpecificProjectComboBox.setOnAction(event -> {
            String selectedProjectNo = viewSpecificProjectComboBox.getValue();
            if (selectedProjectNo != null && !selectedProjectNo.isEmpty()) {
                populateTableViewForProject(selectedProjectNo);
            }
        });
    }

    // Populate TableView for selected project
    private void populateTableViewForProject(String projectNo) {
        try {
            Project project = projectDao.findByProjectNo(projectNo);
            if (project != null) {
                ObservableList<Project> projectList = FXCollections.observableArrayList(project);
                tableViewManageProject.setItems(projectList);
            } else {
                tableViewManageProject.getItems().clear();
            }
        } catch (DaoException e) {
            System.err.println("Error fetching project: " + e.getMessage());
        }
    }

    @FXML
    private Button viewAllProjectsButton;

    @FXML
    private void handleButtonViewAllProjects() {
        try {
            List<Project> projects = projectDao.findAll(); // Fetch all projects
            ObservableList<Project> projectList = FXCollections.observableArrayList(projects); // Convert to ObservableList
            // Set items in TableView
            tableViewManageProject.setItems(projectList); 
        } catch (DaoException e) {
            //Error message
            System.err.println("Error fetching projects: " + e.getMessage());
        }
    }

    private void setUpTableView() {
        tableViewManageProject.setEditable(true);
    }

    private void setupTableColumns() {
        colStatus.setCellValueFactory(new PropertyValueFactory<>("projectStatus"));
        colProjectNo.setCellValueFactory(new PropertyValueFactory<>("projectNo"));
        colProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));

        // Make columns editable except for the date columns
        colProjectNo.setCellFactory(TextFieldTableCell.forTableColumn());
        colProjectName.setCellFactory(TextFieldTableCell.forTableColumn());
        colStartDate.setCellFactory(DatePickerTableCell.forTableColumn());
        colEndDate.setCellFactory(DatePickerTableCell.forTableColumn());

        // Add edit commit handlers
        colProjectNo.setOnEditCommit(event -> updateProject(event.getRowValue(), "projectNo", event.getNewValue()));
        colProjectName.setOnEditCommit(event -> updateProject(event.getRowValue(), "projectName", event.getNewValue()));
        colStartDate
                .setOnEditCommit(event -> updateProject(event.getRowValue(), "projectStartDate", event.getNewValue()));
        colEndDate.setOnEditCommit(event -> updateProject(event.getRowValue(), "projectEndDate", event.getNewValue()));
    }

    private void updateProject(Project project, String field, Object newValue) {
        String oldProjectNo = project.getProjectNo();
        try {
            switch (field) {
                case "projectNo":
                    String projectNo = (String) newValue;
                    // Set to null if the text is empty
                    projectNo = (projectNo != null && projectNo.trim().isEmpty()) ? null : projectNo;
                    project.setProjectNo(projectNo);
                    break;
                case "projectName":
                    String projectName = (String) newValue;
                    // Set to null if the text is empty
                    projectName = (projectName != null && projectName.trim().isEmpty()) ? null : projectName;
                    project.setProjectName(projectName);
                    break;
                case "projectStartDate":
                    project.setProjectStartDate((LocalDate) newValue);
                    break;
                case "projectEndDate":
                    project.setProjectEndDate((LocalDate) newValue);
                    break;
            }
            projectDao.update(project, oldProjectNo);
            tableViewManageProject.refresh();
            // populateProjectNumbers();
            populateViewAllProjectsComboBox();
            manageProjectsResponse.setText("Update successful!");
            manageProjectsResponse.setStyle("-fx-text-fill: green");
        } catch (DaoException e) {
            manageProjectsResponse.setText(e.getMessage());
            manageProjectsResponse.setStyle("-fx-text-fill: red");
            handleButtonViewAllProjects();
            tableViewManageProject.refresh();
        }
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

            registerProjectLabelResponse.setText("");
            registerProjectLabelResponse.setText("Project: " + projectNo + " with name: " + projectName + " has been successfully registered.");
            registerProjectLabelResponse.setStyle("-fx-text-fill: green");
            populateViewAllProjectsComboBox();

            // clear the text fields
            registerProjectNo.clear();
            registerProjectName.clear();
            registerProjectStartDate.getEditor().clear();
            registerProjectEndDate.getEditor().clear();

        } catch (DaoException e) {
            registerProjectLabelResponse.setText(e.getMessage());
            registerProjectLabelResponse.setStyle("-fx-text-fill: red");
        }
    }
}