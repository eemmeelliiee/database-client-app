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

    //DAO instance
    private ConsultantDao consultantDao;

    //Constructor
    public ConsultantTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            consultantDao = new ConsultantDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    //Initialize the controller
    @FXML
    private void initialize() {
        totNbrOfConsultants();
        populateEmployeeNumbers();
        populateEmployeeTitles();
        setupTableColumns();
        setUpTableView();
        populateViewSpecificConsultantComboBox();
        setViewSpecificConsultantComboBoxHandler();
        loadConsultants();
    }

    //Back to Home Page Button
    @FXML
    private Button backToHomePageButton;

    //Button to get to the home page
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

    //Manage consultant pane
    @FXML
    private AnchorPane manageConsultantsPane;

    @FXML
    private Button showManageConsultantsPane;

    @FXML
    private void handleShowManageConsultantsPane() {
        totNbrOfConsultants();
        handleButtonViewAll();
        manageConsultantsPane.setVisible(true);
        registerConsultantPane.setVisible(false);
        registerConsultantResponse.setText("");
    }

    //Register Consultant Pane
    @FXML
    private AnchorPane registerConsultantPane;

    @FXML
    private Button showRegisterConsultantPane;

    @FXML
    private void handleShowRegisterConsultantPane() {
        registerConsultantPane.setVisible(true);
        manageConsultantsPane.setVisible(false);
        infoOverViewLabel.setText("");
        refreshConsultantTable();
    }


    //MANAGE CONSULTANT 

    //Total number of consultants
    private void totNbrOfConsultants() {
        List<Consultant> consultants = consultantDao.findAll();
        totalConsultantsResponse.setText(""+ consultants.size());
    }

    //Set up the TableView
    private void setUpTableView() {
        manageConsultantTableView.setEditable(true);
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @FXML
    private ComboBox<String> viewByTitle;

    //Set up the TableColumns to bind to Consultant properties
    private void setupTableColumns() {
        colEmpNo.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("empFirstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("empLastName"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("empTitle"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("empStartDate"));

        //Make columns editable except for the date column
        colEmpNo.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colStartDate.setCellFactory(DatePickerTableCell.forTableColumn());

        //Add edit commit handlers
        colEmpNo.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empNo", event.getNewValue()));
        colFirstName.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empFirstName", event.getNewValue()));
        colLastName.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empLastName", event.getNewValue()));
        colTitle.setOnEditCommit(event -> updateConsultant(event.getRowValue(), "empTitle", event.getNewValue()));

        //Add a listener to combobox to populate TableView when a title is selected
        viewByTitle.setOnAction(event -> populateTableViewByTitle(viewByTitle.getValue()));
    }

    //Populate the comboboxes with employee titles
    private void populateEmployeeTitles() {
        List<String> employeeTitles = consultantDao.findAllEmpTitles();
        viewByTitle.getItems().clear();
        viewByTitle.getItems().addAll(employeeTitles);
    }

    //Populate the comboboxes with employee numbers
    private void populateEmployeeNumbers() {
        List<String> employeeNumbers = consultantDao.findAllEmpNos();
        viewSpecificConsultantComboBox.getItems().clear();
        viewSpecificConsultantComboBox.getItems().addAll(employeeNumbers);
    }

    //Load consultants into the TableView
    private void loadConsultants() {
        try {
            List<Consultant> consultants = consultantDao.findAll();
            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants);
            manageConsultantTableView.setItems(consultantList);
        } catch (DaoException e) {
            System.err.println("Error loading consultants: " + e.getMessage());
        }
    }

    //View specific consultant
    @FXML
    private ComboBox<String> viewSpecificConsultantComboBox;

    //Set the handler for the viewSpecificConsultantComboBox
    private void setViewSpecificConsultantComboBoxHandler() {
        viewSpecificConsultantComboBox.setOnAction(event -> {
            String selectedEmpNo = viewSpecificConsultantComboBox.getValue();
            if (selectedEmpNo != null && !selectedEmpNo.isEmpty()) {
                updateTableViewForSpecificConsultant(selectedEmpNo);
            }
        });
    }

    // Populate the viewSpecificConsultantComboBox with employee numbers
    private void populateViewSpecificConsultantComboBox() {
        List<String> employeeNumbers = consultantDao.findAllEmpNos();
        viewSpecificConsultantComboBox.getItems().clear();
        viewSpecificConsultantComboBox.getItems().addAll(employeeNumbers);
    }

    // Update the TableView to show the selected consultant
    private void updateTableViewForSpecificConsultant(String empNo) {
        try {
            Consultant consultant = consultantDao.findByEmpNo(empNo);
            if (consultant != null) {
                ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultant);
                manageConsultantTableView.setItems(consultantList);
            } else {
                manageConsultantTableView.getItems().clear();
            }
        } catch (DaoException e) {
            System.err.println("Error fetching consultant: " + e.getMessage());
        }
    }

    //Remove consultant
    @FXML
    private void handleButtonRemoveConsultant() {
        //Get the selected consultant from the TableView
        Consultant selectedConsultant = manageConsultantTableView.getSelectionModel().getSelectedItem();
    
        if (selectedConsultant != null) {
            try {
                String empNo = selectedConsultant.getEmpNo(); 
    
                //Remove the consultant using the empNo
                consultantDao.deleteByEmpNo(empNo); // Remove the consultant
    
                //Create a response message
                String responseMessage = String.format("Consultant with Employee No: " + empNo + " has been successfully removed.");
    
                //Set the formatted string to the response label
                infoOverViewLabel.setText(responseMessage);
                infoOverViewLabel.setStyle("-fx-text-fill: green");
    
                //Optional: Refresh the TableView after removal
                refreshConsultantTable();
                handleButtonViewAll();

            } catch (DaoException e) {
                infoOverViewLabel.setText(e.getMessage());
                infoOverViewLabel.setStyle("-fx-text-fill: red");
            }
        } else {
            // If no consultant is selected, show a warning message
            infoOverViewLabel.setText("Please select a consultant to remove.");
            infoOverViewLabel.setStyle("-fx-text-fill: red");
        }
    }

    //Refresh the consultantTableView
    private void refreshConsultantTable() {
        //Get all consultants from the database
        List<Consultant> updatedConsultants = consultantDao.findAll(); 
        manageConsultantTableView.setItems(FXCollections.observableArrayList(updatedConsultants));
    }
    
    //Update consultant
    @FXML
    private Label infoOverViewLabel;

    @FXML
    private Label totalConsultantsResponse;

    @FXML
    private TableView<Consultant> manageConsultantTableView;

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

    private void updateConsultant(Consultant consultant, String field, Object newValue) {
        //Set to null if the new value is empty and is a String
        if (newValue instanceof String) {
            newValue = (newValue != null && ((String) newValue).trim().isEmpty()) ? null : newValue;
        }

        //Get the old employee number and update the consultant with the new values
        String oldEmpNo = consultant.getEmpNo();
        try {
            switch (field) {
                case "empNo":
                    consultant.setEmpNo((String) newValue);
                    break;
                case "empFirstName":
                    consultant.setEmpFirstName((String) newValue);
                    break;
                case "empLastName":
                    consultant.setEmpLastName((String) newValue);
                    break;
                case "empTitle":
                    consultant.setEmpTitle((String) newValue);
                    break;
                case "empStartDate":
                    consultant.setEmpStartDate((LocalDate) newValue);
                    break;
            }
            consultantDao.update(consultant, oldEmpNo);
            manageConsultantTableView.refresh();
            populateEmployeeNumbers();
            populateEmployeeTitles();
            infoOverViewLabel.setText("Update successful!");
            infoOverViewLabel.setStyle("-fx-text-fill: green");
        } catch (DaoException e) {
            infoOverViewLabel.setText(e.getMessage());
            infoOverViewLabel.setStyle("-fx-text-fill: red");
            handleButtonViewAll();
            manageConsultantTableView.refresh();
        }
    }

    // Populate the TableView based on the selected title
    private void populateTableViewByTitle(String title) {
        if (title != null && !title.isEmpty()) {
            List<Consultant> consultants = consultantDao.findByEmpTitle(title); // Find consultants by title

            // Check
            System.out.println("Consultants retrieved: " + consultants.size());

            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants); // Convert to ObservableList
            manageConsultantTableView.setItems(consultantList); // Set items in TableView
        }
    }

    //View all consultants button and handler
    @FXML
    private Button viewAllButton;

    @FXML
    private void handleButtonViewAll() {
        try {
            List<Consultant> consultants = consultantDao.findAll(); // Fetch all consultants
            ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants); // Convert to
                                                                                                        // ObservableList
            manageConsultantTableView.setItems(consultantList); // Set items in TableView
            totalConsultantsResponse.setText(""+consultants.size());
        } catch (DaoException e) {
            // Handle exception (e.g., show an error message)
            totalConsultantsResponse.setText("Error fetching consultants: " + e.getMessage());
            totalConsultantsResponse.setStyle("-fx-text-fill: red");
        }
    }

    // REGISTER CONSULTANT

    //Textfields, labels and buttons for registering a consultant
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
    private Label registerConsultantResponse;

    @FXML
    private void handleButtonRegisterConsultant() {
        try {
            // Get the values from the text fields
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

            // Create a new Consultant object
            Consultant newConsultant = new Consultant(empNo, empFirstName, empLastName, empTitle, empStartDate);

            // Save the new consultant
            consultantDao.save(newConsultant);

            // Set the response message
            registerConsultantResponse.setText("Consultant '" + empFirstName + " " + empLastName + "', successfully registered");
            populateEmployeeNumbers();
            registerConsultantResponse.setStyle("-fx-text-fill: green");

            // Clear the text fields
            registerConsultantNo.clear();
            registerConsultantName.clear();
            registerConsultantLast.clear();
            registerConsultantTitle.clear();
            registerConsultantDate.getEditor().clear();
            
        } catch (DaoException e) {
            registerConsultantResponse.setText(e.getMessage());
            registerConsultantResponse.setStyle("-fx-text-fill: red");
        }
    }
}