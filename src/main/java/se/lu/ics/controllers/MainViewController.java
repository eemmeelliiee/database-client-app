package se.lu.ics.controllers;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MainViewController {

   //Home page buttons
    @FXML
    private Button showConsultantTabButton;

    @FXML
    private Button showProjectTabButton;

    @FXML
    private Button showMilestoneTabButton;

    @FXML
    private Button showWorkTabButton;

    @FXML
    private Button showMetadataTabButton;

    //Button to get to the consultant tab
    @FXML
    private void handleShowConsultantTabButton(ActionEvent event) {
        String path = "/fxml/ConsultantTab.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            // Load the fxml file and create a new stage for the popup
            SplitPane root = loader.load();

            // Create the new stage
            Stage consultantStage = new Stage();

            // Set the scene
            Scene consultantScene = new Scene(root);

            // Set the scene to the stage
            consultantStage.setScene(consultantScene);

            // Set the title of the stage
            consultantStage.setTitle("Consultant");
            consultantStage.show();

            // Close the current stage
            Stage currentStage = (Stage) showConsultantTabButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Button to get to the project tab
    @FXML
    private void handleShowProjectTabButton(ActionEvent event) {
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
            Stage currentStage = (Stage) showProjectTabButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Button to get to the milestone tab
    @FXML
    private void handleShowMilestoneTabButton(ActionEvent event) {
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
            Stage currentStage = (Stage) showMilestoneTabButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Button to get to the work tab
    @FXML
    private void handleShowWorkTabButton(ActionEvent event) {
        String path = "/fxml/WorkTab.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            SplitPane root = loader.load();
            Stage workStage = new Stage();
            Scene workScene = new Scene(root);
            
            workStage.setScene(workScene);

            workStage.setTitle("Work");
            workStage.show();

            // Close the current stage
            Stage currentStage = (Stage) showWorkTabButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Button to get to the metadata tab
    @FXML
    private void handleShowMetadataTabButton(ActionEvent event) {
        String path = "/fxml/MetadataTab.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            SplitPane root = loader.load();
            Stage metadataStage = new Stage();
            Scene metadataScene = new Scene(root);
            
            metadataStage.setScene(metadataScene);

            metadataStage.setTitle("Metadata");
            metadataStage.show();

            // Close the current stage
            Stage currentStage = (Stage) showMetadataTabButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    

/* Reference to the TabPane (injected from FXML)
@FXML
private TabPane mainTabPane;

//Reference to individual tab controllers (optional if you need them)
private ConsultantTabController consultantTabController;
private ProjectTabController projectTabController;
private MilestoneTabController milestoneTabController;
private WorkTabController workTabController; 

// Initializes the controller class after the root element has been completely processed
@FXML
public void initialize() {
    // Access the controllers for each tab (if needed)
    consultantTabController = loadTabController("consultantTab.fxml");
    projectTabController = loadTabController("projectTab.fxml");
    milestoneTabController = loadTabController("milestoneTab.fxml");
    workTabController = loadTabController("workTab.fxml");
    
    // You can now use tab1Controller, tab2Controller, and tab3Controller to manipulate each tab
    // For example, you can communicate between the tabs using these controllers
}

// DAO instance
// private ConsultantDao consultantDao = new ConsultantDao();*/



