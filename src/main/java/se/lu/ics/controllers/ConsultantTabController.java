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

import se.lu.ics.models.Consultant;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ConnectionHandler;


public class ConsultantTabController {

    // DAO instance
    private ConsultantDao consultantDao;

    // Constructor
    public ConsultantTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            consultantDao = new ConsultantDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        populateEmployeeNumbers();
        populateEmployeeTitles();
        setupTableColumns();
        setUpTableView();
        populateViewSpecificConsultantComboBox();
        setViewSpecificConsultantComboBoxHandler();
        loadConsultants();
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
                    "First Name: " + (empFirstName != null ? empFirstName : "N/A") + "   |   " + 
                    "Last Name: " + (empLastName != null ? empLastName : "N/A") + "\n" +
                    "Title: " + (empTitle != null ? empTitle : "N/A") + "   |   " + 
                    "Employee No: " + empNo + "\n" +
                    "Start Date: " + (empStartDate != null ? empStartDate : "N/A"));

            populateEmployeeNumbers();
            lableResponse.setStyle("-fx-text-fill: green");
        } catch (DaoException e) {
            lableResponse.setText("Error: " + e.getMessage());
            lableResponse.setStyle("-fx-text-fill: red");
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
                removeConsultantResponse.setStyle("-fx-text-fill: green");

                // Optional: Refresh the ComboBox after removal
                populateEmployeeNumbers(); // Assuming you have a method to refresh the ComboBox
            } else {
                // If no Employee Number is selected, show a warning message
                removeConsultantResponse.setText("Please select an employee number.");
            }
        } catch (DaoException e) {
            removeConsultantResponse.setText("Error: " + e.getMessage());
            removeConsultantResponse.setStyle("-fx-text-fill: red");
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
            infoOverViewLabel.setStyle("-fx-text-fill: green");
        } catch (DaoException e) {
            infoOverViewLabel.setText("Error : \n" + e.getMessage());
            infoOverViewLabel.setStyle("-fx-text-fill: red");
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
            totalConsultantsResponse.setText("Error fetching consultants: " + e.getMessage());
            totalConsultantsResponse.setStyle("-fx-text-fill: red");
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

    
}
