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

import se.lu.ics.models.Milestone;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ConnectionHandler;

public class MilestoneTabController {

    private MilestoneDao milestoneDao;

    // Constructor
    public MilestoneTabController() throws IOException {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            milestoneDao = new MilestoneDao(connectionHandler);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
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


    
}
