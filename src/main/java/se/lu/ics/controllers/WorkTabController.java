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

import se.lu.ics.models.Work;
import se.lu.ics.data.WorkDao;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ConnectionHandler;

public class WorkTabController {

    //Dao instance
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

    // Projects involving all consultants
    @FXML Button projectsInvolvingAllConsultants;

    // private void handleButtonProjectsInvolvingAllConsultants(){

    // }
}
