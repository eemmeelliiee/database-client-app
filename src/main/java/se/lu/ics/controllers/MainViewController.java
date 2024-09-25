package se.lu.ics.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.ComboBox;

import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.data.MilestoneDao;

public class MainViewController {

    // DAO instance
    private ConsultantDao consultantDao;

    private ProjectDao projectDao;

    private MilestoneDao milestoneDao;

    // Constructor
    public MainViewController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            consultantDao = new ConsultantDao(connectionHandler);
            projectDao = new ProjectDao(connectionHandler);
            milestoneDao = new MilestoneDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        populateEmployeeNumbers();
        populateEmployeeTitles();
        populateProjectNumbers();
        setupTableColumns();
        setUpTableView();
        populateViewSpecificConsultantComboBox();
        setViewSpecificConsultantComboBoxHandler();
        loadConsultants();
    }

    // Consultant Tab
    // Register Consultant Pane
    @FXML
    private AnchorPane registerConsultantPane;

    @FXML
    private Button showRegisterConsultantPane;

    @FXML
    private void handleShowRegisterConsultantPane() {
        registerConsultantPane.setVisible(true);
        removeConsultantPane.setVisible(false);
        infoPane.setVisible(false);
    }

    // Remove consultant Pane
    @FXML
    private AnchorPane removeConsultantPane;

    @FXML
    private Button showRemoveConsultantPane;

    @FXML
    private void handleShowRemoveConsultantPane() {
        removeConsultantPane.setVisible(true);
        registerConsultantPane.setVisible(false);
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

    // Milestone Tab

    // Remove Milestone Pane
    @FXML
    private AnchorPane removeMSPane;

    @FXML
    private Button showRemoveMSPane;

    @FXML
    private void handleShowRemoveMSPane() {
        removeMSPane.setVisible(true);
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
        removeMSPane.setVisible(false);
        addMSPane.setVisible(false);
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
    private Button showCheckWorkingConsPane;

    @FXML
    private void handleShowCheckWorkingConsPane() {
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

    // MetaData Tab
    //

    // CONSULTANT TAB
    private void setUpTableView() {
        consultantTableView.setEditable(true);
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());

        // colTitle.setOnEditCommit(event -> {
        //     Consultant consultant = event.getRowValue();
        //     String newTitle = event.getNewValue();
        //     // consultant.setEmpTitle(newTitle);

        //     if (!consultant.getEmpTitle().equals(newTitle)) {
        //         consultant.setEmpTitle(newTitle);

        //         consultantDao.update(consultant, consultant.getEmpNo());

        //         populateEmployeeTitles();

        //         event.getTableView().getItems().set(event.getTablePosition().getRow(), consultant);

        //         consultantTableView.refresh();
        //     }
        // });
    }

    // Set up the TableColumns to bind to Consultant properties
    private void setupTableColumns() {
    colEmpNo.setCellValueFactory(new PropertyValueFactory<>("empNo"));
    colFirstName.setCellValueFactory(new PropertyValueFactory<>("empFirstName"));
    colLastName.setCellValueFactory(new PropertyValueFactory<>("empLastName"));
    colTitle.setCellValueFactory(new PropertyValueFactory<>("empTitle"));
    colStartDate.setCellValueFactory(new PropertyValueFactory<>("empStartDate"));

    // Make columns editable except for the date column
    colEmpNo.setCellFactory(TextFieldTableCell.forTableColumn());
    colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
    colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
    colTitle.setCellFactory(TextFieldTableCell.forTableColumn());

    // Add edit commit handlers
    colEmpNo.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empNo", event.getNewValue()));
    colFirstName.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empFirstName", event.getNewValue()));
    colLastName.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empLastName", event.getNewValue()));
    colTitle.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empTitle", event.getNewValue()));

    // Add a listener to combobox to populate TableView when a title is selected
    infoConsultantTitle.setOnAction(event -> populateTableViewByTitle(infoConsultantTitle.getValue()));
}

    private void populateEmployeeTitles() {
        List<String> employeeTitles = consultantDao.findAllEmpTitles();
        infoConsultantTitle.getItems().clear();
        infoConsultantTitle.getItems().addAll(employeeTitles);
    }

    private void populateEmployeeNumbers() {
        List<String> employeeNumbers = consultantDao.findAllEmpNos();
        removeConsultantNo.getItems().clear();
        removeConsultantNo.getItems().addAll(employeeNumbers);
        viewSpecificConsultantComboBox.getItems().clear();
        viewSpecificConsultantComboBox.getItems().addAll(employeeNumbers);

        /*
         * infoConsultantNo.getItems().clear();
         * infoConsultantNo.getItems().addAll(employeeNumbers);
         */
    }

    private void loadConsultants() {
        try {
            List<Consultant> consultants = consultantDao.findAll();
            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants);
            consultantTableView.setItems(consultantList);
        } catch (DaoException e) {
            System.err.println("Error loading consultants: " + e.getMessage());
        }
    }

    // REGISTER CONSULTANT

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

    @FXML
    private void handleButtonRegisterConsultant() {
        try {
            String empNo = registerConsultantNo.getText();
            String empFirstName = registerConsultantName.getText();
            String empLastName = registerConsultantLast.getText();
            String empTitle = registerConsultantTitle.getText();
            LocalDate empStartDate = registerConsultantDate.getValue();

            // Set to null if the text is empty
            empNo = (empNo != null && empNo.trim().isEmpty()) ? null : empNo;
            empFirstName = (empFirstName != null && empFirstName.trim().isEmpty()) ? null : empFirstName;
            empLastName = (empLastName != null && empLastName.trim().isEmpty()) ? null : empLastName;
            empTitle = (empTitle != null && empTitle.trim().isEmpty()) ? null : empTitle;

            Consultant newConsultant = new Consultant(empNo, empFirstName, empLastName, empTitle, empStartDate);

            consultantDao.save(newConsultant);

            lableResponse.setText("New Consultant Registered:\n" +
                    "First Name: " + (empFirstName != null ? empFirstName : "N/A") + "\n" +
                    "Last Name: " + (empLastName != null ? empLastName : "N/A") + "\n" +
                    "Title: " + (empTitle != null ? empTitle : "N/A") + "\n" +
                    "Employee No: " + empNo + "\n" +
                    "Start Date: " + (empStartDate != null ? empStartDate : "N/A"));

            populateEmployeeNumbers();
        } catch (DaoException e) {
            lableResponse.setText("Error: " + e.getMessage());
        }

    }

    // REMOVE CONSULTANT

    @FXML
    private ComboBox<String> removeConsultantNo; // combobox for employee number

    @FXML
    private Button removeConsultantButton;

    @FXML
    private Label removeConsultantResponse;

    @FXML
    private void handleButtonRemoveConsultant() {
        try {
            String empNo = removeConsultantNo.getValue(); // Get the selected Employee Number

            // Check if an Employee Number is selected
            if (empNo != null && !empNo.isEmpty()) {
                consultantDao.deleteByEmpNo(empNo); // Remove the consultant

                // Create a response message with a newline for formatting
                String responseMessage = String
                        .format("Consultant with Employee No: " + empNo + " has been successfully removed.");

                // Set the formatted string to the response label
                removeConsultantResponse.setText(responseMessage);

                // Optional: Refresh the ComboBox after removal
                populateEmployeeNumbers(); // Assuming you have a method to refresh the ComboBox
            } else {
                // If no Employee Number is selected, show a warning message
                removeConsultantResponse.setText("Please select an employee number.");
            }
        } catch (DaoException e) {
            removeConsultantResponse.setText("Error: " + e.getMessage());
        }
    }

    // Info consultant
    /*
     * @FXML
     * private ComboBox<String> infoConsultantNo; // combobox for employee number
     */

    // @FXML
    // private Button infoConsultantButton;

    // INFO OVERVIEW CONSULTANT

    @FXML
    private ComboBox<String> infoConsultantTitle; // combobox for employee name

    @FXML
    private Label infoConsultantResponse;

    @FXML
    private Label infoOverViewLabel;

    @FXML
    private Label totalConsultantsResponse;

    @FXML
    private TableView<Consultant> consultantTableView;

    @FXML
    private TableColumn<Consultant, String> colEmpNo; // Column for employee number
    @FXML
    private TableColumn<Consultant, String> colFirstName; // Column for first name
    @FXML
    private TableColumn<Consultant, String> colLastName; // Column for last name
    @FXML
    private TableColumn<Consultant, String> colTitle; // Column for title
    @FXML
    private TableColumn<Consultant, LocalDate> colStartDate; // Column for start date

    private void updateConsultant(Consultant consultant, String field, String newValue) {
        String oldEmpNo = consultant.getEmpNo();
        try {
            switch (field) {
                case "empNo":
                    consultant.setEmpNo(newValue);
                    break;
                case "empFirstName":
                    consultant.setEmpFirstName(newValue);
                    break;
                case "empLastName":
                    consultant.setEmpLastName(newValue);
                    break;
                case "empTitle":
                    consultant.setEmpTitle(newValue);
                    break;
            }
            consultantDao.update(consultant, oldEmpNo);
            consultantTableView.refresh();
            populateEmployeeNumbers();
            populateEmployeeTitles();
            infoOverViewLabel.setText("Update successful!");  
            } catch (DaoException e) {
                infoOverViewLabel.setText("Error : " + e.getMessage());
                handleButtonViewAll();
                consultantTableView.refresh();

            }
        }

    // Populate the TableView based on the selected title
    private void populateTableViewByTitle(String title) {
        if (title != null && !title.isEmpty()) {
            List<Consultant> consultants = consultantDao.findByEmpTitle(title); // Find consultants by title

            // Check
            System.out.println("Consultants retrieved: " + consultants.size());

            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants); // Convert to
                                                                                                        // ObservableList
            consultantTableView.setItems(consultantList); // Set items in TableView
        }
    }

    @FXML
    private Button viewAllButton;

    @FXML
    private void handleButtonViewAll() {
        try {
            List<Consultant> consultants = consultantDao.findAll(); // Fetch all consultants
            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants); // Convert to
                                                                                                        // ObservableList
            consultantTableView.setItems(consultantList); // Set items in TableView
            totalConsultantsResponse.setText("Total amount\nof consultants: " + consultants.size());
        } catch (DaoException e) {
            // Handle exception (e.g., show an error message)
            System.err.println("Error fetching consultants: " + e.getMessage());
        }
    }

    @FXML
    private Label infoOverviewLabel;

    @FXML
    private ComboBox<String> viewSpecificConsultantComboBox;

    private void setViewSpecificConsultantComboBoxHandler() {
        viewSpecificConsultantComboBox.setOnAction(event -> {
            String selectedEmpNo = viewSpecificConsultantComboBox.getValue();
            if (selectedEmpNo != null && !selectedEmpNo.isEmpty()) {
                updateTableViewForSpecificConsultant(selectedEmpNo);
            }
        });
    }

    private void updateTableViewForSpecificConsultant(String empNo) {
        try {
            Consultant consultant = consultantDao.findByEmpNo(empNo);
            if (consultant != null) {
                ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultant);
                consultantTableView.setItems(consultantList);
            } else {
                consultantTableView.getItems().clear();
            }
        } catch (DaoException e) {
            System.err.println("Error fetching consultant: " + e.getMessage());

        }
    }

    private void populateViewSpecificConsultantComboBox() {
        List<String> employeeNumbers = consultantDao.findAllEmpNos();
        viewSpecificConsultantComboBox.getItems().clear();
        viewSpecificConsultantComboBox.getItems().addAll(employeeNumbers);
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
        milestoneProjectNo.getItems().clear();
        milestoneProjectNo.getItems().addAll(projectNumbers);

        // infoProjectNo.getItems().clear();
        // infoProjectNo.getItems().addAll(projectNumbers);
    }




    // Register Milestone
    @FXML
    private TextField registerMilestoneNo;

    @FXML
    private TextField registerMilestoneName;

    @FXML
    private DatePicker registerMilestoneDate;

    @FXML
    private Button registerMilestoneButton;

    @FXML
    private ComboBox<String> milestoneProjectNo;

    @FXML
    private Label registerMilestoneLabelResponse;


    @FXML
    private void handleButtonRegisterMilestone() {
        try {
            String milestoneNo = registerMilestoneNo.getText();
            String milestoneName = registerMilestoneName.getText();
            LocalDate milestoneDate = registerMilestoneDate.getValue();
            String projectNo = milestoneProjectNo.getValue();

            // Set to null if the text is empty
            milestoneNo = (milestoneNo != null && milestoneNo.trim().isEmpty()) ? null : milestoneNo;
            milestoneName = (milestoneName != null && milestoneName.trim().isEmpty()) ? null : milestoneName;

            Milestone newMilestone = new Milestone(milestoneNo, projectNo, milestoneName, milestoneDate);

            milestoneDao.save(newMilestone);

            registerMilestoneLabelResponse.setText(
                    "New Milestone Registered:\n" +
                            "Milestone Name: " + (milestoneName != null ? milestoneName : "N/A") + "\n" +
                            "Milestone No: " + (milestoneNo != null ? milestoneNo : "N/A") + "\n" +
                            "Date: " + (milestoneDate != null ? milestoneDate : "N/A") + "\n" +
                            "Project No: " + (projectNo != null ? projectNo : "N/A"));

            populateProjectNumbers();
        } catch (DaoException e) {
            registerMilestoneLabelResponse.setText(e.getMessage());
        }
    }





}
