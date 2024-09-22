package se.lu.ics;

import java.io.File;
import java.io.IOException;

import java.time.LocalDate;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.MetaDataDao; 
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;
import java.awt.Desktop;

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

    // Used for opening the xlsx file. Should probably be moved to a controller
    private static void openExcelFile(String filePath) {
        try {
            File file = new File(filePath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file); // Open the file with the default application
            } else {
                System.err.println("Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            System.err.println("Error opening Excel file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            ConsultantDao consultantDao = new ConsultantDao(connectionHandler);
            ProjectDao projectDao = new ProjectDao(connectionHandler);
            WorkDao workDao = new WorkDao(connectionHandler, consultantDao, projectDao);
            MetaDataDao metaDataDao = new MetaDataDao(connectionHandler);


            // PROJECT TESTS

            // 1. Test saving a new project
            Project newProject = new Project("P1001", "AI Development", LocalDate.of(2023, 1, 15),
            LocalDate.of(2023, 12, 31));
            projectDao.save(newProject);
            System.out.println("Project saved successfully: " + newProject);

            // 2. Test finding a project by project number
            System.out.println("\nRetrieving project by ProjectNo 'P1001':");
            Project foundProject = projectDao.findByProjectNo(newProject.getProjectNo());
            System.out.println("Project found: " + foundProject);

            // 3. Test updating the project
            foundProject.setProjectName("AI Development Updated");
            foundProject.setProjectEndDate(LocalDate.of(2024, 1, 31));
            projectDao.update(foundProject);
            System.out.println("\nProject updated successfully: " + foundProject);

            // 4. Test retrieving all projects
            System.out.println("\nRetrieving all projects:");
            List<Project> allProjects = projectDao.findAll();
            for (Project project : allProjects) {
                System.out.println(project);
            }

            // 5. Test deleting the project by project number
            projectDao.deleteByProjectNo(newProject.getProjectNo());
            System.out.println("\nProject deleted successfully: P1001");

            // 6. Verify that the project is deleted
            Project deletedProject = projectDao.findByProjectNo(newProject.getProjectNo());
            if (deletedProject == null) {
                System.out.println("Project P1001 successfully deleted from the database.");
            }


            // CONSULTANT TESTS

            // 1. Test retrieving all consultants
            System.out.println("Retrieving all consultants:");
            List<Consultant> allConsultants = consultantDao.findAll();
            for (Consultant consultant : allConsultants) {
                System.out.println(consultant);
            }

            // 2. Test saving a new consultant
            System.out.println("\nSaving a new consultant:");
            Consultant newConsultant = new Consultant("EMP1001", "John", "Doe", "Senior Consultant",
            LocalDate.of(2020, 1, 15));
            consultantDao.save(newConsultant);
            System.out.println("Consultant saved: " + newConsultant);

            // 3. Test retrieving a consultant by EmpNo
            System.out.println("\nRetrieving consultant by EmpNo:");
            Consultant consultantByEmpNo = consultantDao.findByEmpNo(newConsultant.getEmpNo());
            System.out.println("Consultant found: " + consultantByEmpNo);

            // 4. Test updating a consultant
            System.out.println("\nUpdating consultant:");
            consultantByEmpNo.setEmpLastName("Smith");
            consultantDao.update(consultantByEmpNo);
            System.out.println("Consultant updated: " + consultantByEmpNo);

            // 5. Test retrieving consultants by title
            System.out.println("\nRetrieving consultants by title 'Senior IT':");
            List<Consultant> consultantsByTitle = consultantDao.findByEmpTitle("Senior IT");
            for (Consultant consultant : consultantsByTitle) {
                System.out.println(consultant);
            }

            // 6. Test deleting a consultant by EmpNo
            System.out.println("\nDeleting consultant by EmpNo:");
            consultantDao.deleteByEmpNo(newConsultant.getEmpNo());
            System.out.println("Consultant with EmpNo 'EMP1001' deleted.");

            // 7. Test retrieving the total number of consultants
            System.out.println("\nRetrieving total number of consultants:");
            int totalConsultants = consultantDao.getTotalNumberOfConsultants();
            System.out.println("Total number of consultants: " + totalConsultants);


            // WORK TESTS


             // Test addConsultantToProject
             Work work = new Work ("E003","P002",50);
             workDao.addConsultantToProject(work.getEmpNo(), work.getProjectNo(), work.getWorkHours());
             System.out.println("Consultant added to project successfully.");


            // Test listConsultantsByProject
            List<Consultant> consultants = workDao.listConsultantsByProject(work.getProjectNo());
            System.out.println("Consultants working on project P002:");
            for (Consultant consultant : consultants) {
                System.out.println(consultant);
            }

            // Test getTotalWorkedHoursForConsultant
            double totalHours = workDao.getTotalWorkedHoursForConsultant(work.getEmpNo());
            System.out.println("Total worked hours for consultant E003: " + totalHours);

            List<Consultant> consultantsWithThreeProjectsOrLess = workDao.getConsultantsWithThreeProjectsOrLess();
            System.out.println("Consultants with three projects or less:");
            for (Consultant consultant : consultantsWithThreeProjectsOrLess) {
                int nbrOfProjects = workDao.getNumberOfProjectsForConsultant(consultant.getEmpNo());
                System.out.println("Consultant: " + consultant + ", Number of Projects: " + nbrOfProjects);
            }

            // Test getProjectsInvolvingAllConsultants
            List<Project> projectsInvolvingAllConsultants = workDao.getProjectsInvolvingAllConsultants();
            System.out.println("Projects involving all consultants:");
            for (Project project : projectsInvolvingAllConsultants) {
                System.out.println(project);
            }

            // Test getTotalHoursWorked
            double totalHoursWorked = workDao.getTotalHoursWorked();
            System.out.println("Total hours worked by all consultants: " + totalHoursWorked);
            // Test updateWorkHours
            workDao.updateWorkHours(work.getProjectNo(), work.getEmpNo(), work.getWorkHours());
            System.out.println("Work hours updated successfully.");
            // Test getHardestWorkingConsultant
            Consultant hardestWorkingConsultant = workDao.getHardestWorkingConsultant();
            if (hardestWorkingConsultant != null) {
                double totalWorkHours = workDao.getTotalWorkHoursForConsultant(hardestWorkingConsultant.getEmpNo());
                System.out.println("Hardest working consultant: " + hardestWorkingConsultant + ", Total Work Hours: "
                        + totalWorkHours);
            } else {
                System.out.println("No consultant found.");
            }

             // Test deleteConsultantFromProject
             workDao.deleteConsultantFromProject("E003", "P002");
             System.out.println("Consultant with EmpNo E003 removed from project P002 successfully.");


            // TESTS METADATA (not all methods tested)

            // Test getNonIntegerColumns method
            List<String> nonIntegerColumns = metaDataDao.getNonIntegerColumns();
            System.out.println("Non-Integer Columns in Consultant Table:");
            for (String column : nonIntegerColumns) {
                System.out.println(column);
            }

            // Test getTableWithMostRows method
            String[] tableInfo = metaDataDao.getTableWithMostRows();
            System.out.println("Table with Most Rows:");
            System.out.println("Table Name: " + tableInfo[0]);
            System.out.println("Row Count: " + tableInfo[1]);


            // TEST OPEN EXCEL FILE

            openExcelFile("src/main/resources/excel/ExcelData.xlsx");

        } catch (DaoException | IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }

        launch(); // Start the JavaFX application
    }
}
