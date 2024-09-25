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

    //Table information Project Pane
    @FXML
    private AnchorPane tableInfoPane;
  
    @FXML
    private Button showTableInfoPaneButton;
  
    @FXML
    private void handleShowTableInfoPaneButton() {
          tableInfoPane.setVisible(true);
          checkConstraintPane.setVisible(false);
          primaryConstraintPane.setVisible(false);
    }
      
    //Check constraint pane
    @FXML
    private AnchorPane checkConstraintPane;

    @FXML
    private Button showCheckConstraintPaneButton;

    @FXML
    private void handleShowCheckConstraintPaneButton() {
        checkConstraintPane.setVisible(true);
        tableInfoPane.setVisible(false);
        primaryConstraintPane.setVisible(false);
    }

    //Primary constraint pane
    @FXML
    private AnchorPane primaryConstraintPane;

    @FXML
    private Button showPrimaryConstraintPaneButton;

    @FXML
    private void handleShowPrimaryConstraintPaneButton() {
        primaryConstraintPane.setVisible(true);
        tableInfoPane.setVisible(false);
        checkConstraintPane.setVisible(false);
    }


  
    //Get non-integer columns button
    @FXML
    private Button nonIntNamesButton;

    // Get non-integer columns
    @FXML
    private void handleNonIntNamesButton(ActionEvent event) {
        List<String> nonIntNames = metadataDao.getNonIntegerColumns();
        String nonIntNamesString = String.join(", ", nonIntNames);
    }

    //Table with most rows button
    @FXML
    private Button mostRowsButton;

    // Get table with most rows
    @FXML
    private void handleMostRowsButton(ActionEvent event) {
        String[] tableWithMostRows = metadataDao.getTableWithMostRows();
        String tableWithMostRowsString = String.join(", ", tableWithMostRows);
    }

    //All columns button
    @FXML
    private Button allColumnsButton;

    // Get all columns
    @FXML
    private void handleAllColumnsButton(ActionEvent event) {
        List<String> allColumns = metadataDao.getAllColumns();
        String allColumnsString = String.join(", ", allColumns);
    }





    
}
