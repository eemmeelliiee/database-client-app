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
import javafx.scene.control.ComboBox;

import se.lu.ics.data.ProjectDao;
import se.lu.ics.models.Milestone;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ConnectionHandler;

public class MilestoneTabController {

    private MilestoneDao milestoneDao;

    private ProjectDao projectDao;

    // Constructor
    public MilestoneTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            milestoneDao = new MilestoneDao(connectionHandler);
            projectDao = new ProjectDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        populateProjectNumbers();
        setupMilestoneTableView();
    }

    // Back to Project Tab
    @FXML
    private Button backToProjectButton;

    @FXML
    private void handleShowProjectPageButton(ActionEvent event) {
        String path = "/fxml/ProjectTab.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            SplitPane root = loader.load();
            Stage projectStage = new Stage();
            Scene projectScene = new Scene(root);
            
            projectStage.setScene(projectScene);

            projectStage.setTitle("Project");
            projectStage.show();

            // Close the current stage
            Stage currentStage = (Stage) backToProjectButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Add Milestone Pane
    @FXML
    private AnchorPane addMSPane;

    @FXML
    private Button showAddMSPane;

    @FXML
    private void handleShowAddMSPane() {
        addMSPane.setVisible(true);
        manageMSPane.setVisible(false);
        manageMSResponse.setText("");
    }

    //Manage milestones pane
    @FXML
    private AnchorPane manageMSPane;

    @FXML
    private Button showManageMSPane;

    @FXML
    private void handleShowManageMSPane() {
        manageMSPane.setVisible(true);
        addMSPane.setVisible(false);
        registerMilestoneStatus.setText("");
    }

    //Register milestone
    @FXML
    private TextField registerMilestoneNo;

    @FXML
    private ComboBox<String> registerMilestoneProjectNo;

    @FXML
    private DatePicker registerMilestoneDate;

    @FXML
    private TextField registerMilestoneName;

    @FXML
    private Label registerMilestoneStatus;

    @FXML
    private Button registerMilestoneButton;

    @FXML
    private void handleButtonRegisterMilestone() {
        try {
            String milestoneNo = registerMilestoneNo.getText();
            String milestoneName = registerMilestoneName.getText();
            LocalDate milestoneDate = registerMilestoneDate.getValue();
            String projectNo = registerMilestoneProjectNo.getValue();

            // Set to null if the text is empty
            milestoneNo = (milestoneNo != null && milestoneNo.trim().isEmpty()) ? null : milestoneNo;
            milestoneName = (milestoneName != null && milestoneName.trim().isEmpty()) ? null : milestoneName;

            Milestone newMilestone = new Milestone(milestoneNo, projectNo, milestoneName, milestoneDate);

            milestoneDao.save(newMilestone);

            registerMilestoneStatus.setText("Milestone '" + milestoneNo + "'' sucessfully added to '" + projectNo + "'");
            registerMilestoneStatus.setStyle("-fx-text-fill: green");
            populateProjectNumbers();

        } catch (DaoException e) {
            registerMilestoneStatus.setText(e.getMessage());
            registerMilestoneStatus.setStyle("-fx-text-fill: red");
        }
    }

    // populate project numbers
    @FXML
    private void populateProjectNumbers() {
        try {
            List<String> projectNumbers = projectDao.findAllProjectNos();
            ObservableList<String> projectNumbersList = FXCollections.observableArrayList(projectNumbers);
            registerMilestoneProjectNo.setItems(projectNumbersList);
            // removeMilestoneProjectNo.setItems(projectNumbersList);
            manageMSProjectNoComboBox.setItems(projectNumbersList);
        } catch (DaoException e) {
            registerMilestoneStatus.setText(e.getMessage());
            manageMSResponse.setText(e.getMessage()); // ta bort?
        }
    }

    //Manage Milestone
    @FXML
    private TableView<Milestone> manageMSTableView;

    @FXML
    private TableColumn<Milestone, String> milestoneNoColumn;

    @FXML
    private TableColumn<Milestone, String> milestoneNameColumn;

    @FXML
    private TableColumn<Milestone, LocalDate> milestoneDateColumn;

    @FXML
    private ComboBox<String> manageMSProjectNoComboBox;

    @FXML
    private Label manageMSResponse;

    @FXML
    private Label totalMilestonesLabel;

    @FXML
    private Button buttonRemoveMilestone;

    @FXML
    private void handleButtonRemoveMilestone() {
        // Get the selected milestone from the TableView
        Milestone selectedMilestone = manageMSTableView.getSelectionModel().getSelectedItem();

        if (selectedMilestone != null) {
            try {
                // Attempt to delete the selected milestone from the database
                milestoneDao.deleteByProjectNoAndMilestoneNo(manageMSProjectNoComboBox.getValue(), selectedMilestone.getMilestoneNo());

                // Show a confirmation message in the response label
                manageMSResponse.setStyle("-fx-text-fill: green");
                manageMSResponse.setText("Milestone " + selectedMilestone.getMilestoneNo() + " successfully removed.");

                // Refresh the table view after deletion
                onProjectSelected();  // This will re-fetch and display the updated list of milestones

            } catch (DaoException e) {
                // If there's an error in the deletion process, display the error message
                manageMSResponse.setStyle("-fx-text-fill: red");
                manageMSResponse.setText("Error removing milestone: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // If no milestone is selected, display a warning message
            manageMSResponse.setStyle("-fx-text-fill: red");
            manageMSResponse.setText("Please select a milestone to remove.");
        }
    }

    //Set up the Milestone Table
    private void setupMilestoneTableView() {
        //Ensuree the tableView is not editable
        manageMSTableView.setEditable(false);

        //Bind colums to the milestone fields using PropertyValueFactory
        milestoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("milestoneNo"));
        milestoneNameColumn.setCellValueFactory(new PropertyValueFactory<>("milestoneName"));
        milestoneDateColumn.setCellValueFactory(new PropertyValueFactory<>("milestoneDate"));

        //Intially, clear the table view
        manageMSTableView.getItems().clear();
    }

    //Get milestones for selected project
    @FXML
    private void onProjectSelected() {
        String selectedProjectNo = manageMSProjectNoComboBox.getSelectionModel().getSelectedItem();

        if (selectedProjectNo != null) {
            try {
                List<Milestone> milestones = milestoneDao.findByProjectNo(selectedProjectNo);
                ObservableList<Milestone> milestoneList = FXCollections.observableArrayList(milestones);
                manageMSTableView.setItems(milestoneList);
                totalMilestonesLabel.setText("" + milestones.size());
            } catch (DaoException e) {
                manageMSResponse.setText("Error fetching milestones: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            manageMSResponse.setText("Please select a valid project number.");
        }
    }
}