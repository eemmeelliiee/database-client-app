package se.lu.ics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {

        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();

            ConsultantDao consultantDao = new ConsultantDao(connectionHandler);
            ProjectDao projectDao = new ProjectDao(connectionHandler);
            WorkDao workDao = new WorkDao(connectionHandler, consultantDao, projectDao);

            // // Test deleteConsultantFromProject
            // workDao.deleteConsultantFromProject("E003", "P002");
            // System.out.println("Consultant with EmpNo E003 removed from project P002 successfully.");

             // Test addConsultantToProject
            workDao.addConsultantToProject("E009", "P001", 40);
            System.out.println("Consultant added to project successfully.");

            // // Test listConsultantsByProject
            // List<Consultant> consultants = workDao.listConsultantsByProject("P002");
            // System.out.println("Consultants working on project P002:");
            // for (Consultant consultant : consultants) {
            //     System.out.println(consultant);
            // }

            // // Test getTotalWorkedHoursForConsultant
            // int totalHours = workDao.getTotalWorkedHoursForConsultant("E003");
            // System.out.println("Total worked hours for consultant E003: " + totalHours);

            // List<Consultant> consultantsWithThreeProjectsOrLess = workDao.getConsultantsWithThreeProjectsOrLess();
            // System.out.println("Consultants with three projects or less:");
            // for (Consultant consultant : consultantsWithThreeProjectsOrLess) {
            //     int nbrOfProjects = workDao.getNumberOfProjectsForConsultant(consultant.getEmpNo());
            //     System.out.println("Consultant: " + consultant + ", Number of Projects: " + nbrOfProjects );
            // }
            

            // // Test getProjectsInvolvingAllConsultants
            // List<Project> projectsInvolvingAllConsultants = workDao.getProjectsInvolvingAllConsultants();
            // System.out.println("Projects involving all consultants:");
            // for (Project project : projectsInvolvingAllConsultants) {
            //     System.out.println(project);
            // }

            //  // Test getTotalHoursWorked
            //  int totalHoursWorked = workDao.getTotalHoursWorked();
            //  System.out.println("Total hours worked by all consultants: " + totalHoursWorked);
 
            //  // Test updateWorkHours
            //  workDao.updateWorkHours("P002", "E003", 50);
            //  System.out.println("Work hours updated successfully.");
 
            //  // Test getHardestWorkingConsultant
            //  Consultant hardestWorkingConsultant = workDao.getHardestWorkingConsultant();
            //  if (hardestWorkingConsultant != null) {
            //      int totalWorkHours = workDao.getTotalWorkHoursForConsultant(hardestWorkingConsultant.getEmpNo());
            //      System.out.println("Hardest working consultant: " + hardestWorkingConsultant + ", Total Work Hours: " + totalWorkHours);
            //  } else {
            //      System.out.println("No consultant found.");
            //  }
            // Here goes testing code
            // Test 1:

        } catch (DaoException | IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
        launch();

    }


}