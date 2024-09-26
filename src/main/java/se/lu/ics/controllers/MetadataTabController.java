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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.ComboBox;
import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;

import se.lu.ics.data.DaoException;
import se.lu.ics.data.MetaDataDao;
import se.lu.ics.data.ConnectionHandler;

public class MetadataTabController {

    // Dao instance
    private MetaDataDao metadataDao;

    // Constructor
    public MetadataTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            metadataDao = new MetaDataDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Back to Home Page Button
    @FXML
    private Button backToHomePageButton;

    //Handler to get to the home page
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

    //Table information Metadata Pane
    @FXML
    private AnchorPane tableInfoPane;
  
    @FXML
    private Button showTableInfoPaneButton;
  
    @FXML
    private void handleShowTableInfoPaneButton() {
        tableViewColumnNames.getItems().clear();
        tableViewMostRows.getItems().clear();
        tableInfoPane.setVisible(true);
        checkConstraintPane.setVisible(false);
        primaryKeyConstraintPane.setVisible(false);
    }
      
    //Check constraint pane
    @FXML
    private AnchorPane checkConstraintPane;

    @FXML
    private Button showCheckConstraintPaneButton;

    @FXML
    private void handleShowCheckConstraintPaneButton() {
        tableViewCheckConstraints.getItems().clear();
        checkConstraintPane.setVisible(true);
        tableInfoPane.setVisible(false);
        primaryKeyConstraintPane.setVisible(false);
    }

    //Primary constraint pane
    @FXML
    private AnchorPane primaryKeyConstraintPane;

    @FXML
    private Button showPrimaryKeyConstraintPaneButton;

    @FXML
    private void handleShowPrimaryKeyConstraintPaneButton() {
        tableViewPrimaryKeyConstraints.getItems().clear();
        primaryKeyConstraintPane.setVisible(true);
        tableInfoPane.setVisible(false);
        checkConstraintPane.setVisible(false);
    }

    //Show non-int column table names in tableView
    @FXML
    private Button nonIntNamesButton;

    @FXML
    private Button allColumnsButton;
    
    @FXML
    private TableView<String> tableViewColumnNames;

    @FXML
    private TableColumn<String, String> columnNamesColumn;

    @FXML
    private void handleColumnNamesButtons(ActionEvent event) {
        // Identify which button was clicked
        Button clickedButton = (Button) event.getSource();

        List<String> allColumns = new ArrayList<>();

        // Check which button was pressed and fetch the appropriate data
        if (clickedButton == nonIntNamesButton) {
            // Fetch non-integer columns from Consultant table
            allColumns = metadataDao.getNonIntegerColumns();  // Fetch non-integer column names
        } else if (clickedButton == allColumnsButton) {
            // Fetch distinct column names from all tables in the database
            allColumns = metadataDao.getAllColumns();  // Fetch distinct column names
        }

        // Convert the list to an ObservableList for TableView
        ObservableList<String> observableColumnNames = FXCollections.observableArrayList(allColumns);

        // Set the items for the TableView
        tableViewColumnNames.setItems(observableColumnNames);

        // Bind the TableColumn to display the values in the list
        columnNamesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    }


    //Table with most rows button
    @FXML
    private Button mostRowsButton;

    @FXML
    private TableView<String[]> tableViewMostRows;

    @FXML
    private TableColumn<String[], String> mostRowsNameColumn;

    @FXML
    private TableColumn<String[], String> mostRowsNrColumn;

    //Get table with most rows 
    @FXML
    private void handleMostRowsButton(ActionEvent event) {
        // Fetch the table with the most rows
        List<String[]> tableWithMostRows = metadataDao.getTableWithMostRows();

        // Create an ObservableList for the TableView
        ObservableList<String[]> observableMostRows = FXCollections.observableArrayList(tableWithMostRows);

        // Set the items for the TableView
        tableViewMostRows.setItems(observableMostRows);

        // Bind the TableColumn to display the values in the list
        mostRowsNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        mostRowsNrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
    }

    //Table with check constraints button
    @FXML
    private Button checkConstraintButton;

    @FXML
    private TableView<String> tableViewCheckConstraints;

    @FXML
    private TableColumn<String, String> checkConstraintColumn;

    @FXML
    private Label checkConstraintsResponse;

    //Get table with check constraints
    @FXML
    private void handleCheckConstraintButton(ActionEvent event) {
        try {
            List<String> checkConstraints = metadataDao.getCheckConstraints();
            ObservableList<String> observableCheckConstraints = FXCollections.observableArrayList(checkConstraints);
            tableViewCheckConstraints.setItems(observableCheckConstraints);
            checkConstraintColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        } catch (DaoException e) {
            checkConstraintsResponse.setText("Error: " + e.getMessage());
            checkConstraintsResponse.setStyle("-fx-text-fill: red");
        }
    }

    //Table with primary constraints button
    @FXML
    private Button primaryKeyConstraintButton;

    @FXML
    private TableView<String> tableViewPrimaryKeyConstraints;

    @FXML
    private TableColumn<String, String> primaryKeyConstraintColumn;

    @FXML
    private Label primaryKeyConstraintsResponse;

    //Get table with primary constraints
    @FXML
    private void handlePrimaryKeyConstraintButton(ActionEvent event) {
        try {
            List<String> primaryKeyConstraints = metadataDao.getPrimaryKeyConstraints();
            ObservableList<String> observablePrimaryKeyConstraints = FXCollections.observableArrayList(primaryKeyConstraints);
            tableViewPrimaryKeyConstraints.setItems(observablePrimaryKeyConstraints);
            primaryKeyConstraintColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        } catch (DaoException e) {
            primaryKeyConstraintsResponse.setText("Error: " + e.getMessage());
            primaryKeyConstraintsResponse.setStyle("-fx-text-fill: red");
        }
    }
}
