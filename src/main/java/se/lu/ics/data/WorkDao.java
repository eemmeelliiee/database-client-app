package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

public class WorkDao {

    private ConnectionHandler connectionHandler;
    private ConsultantDao consultantDao;
    private ProjectDao projectDao;

    public WorkDao(ConnectionHandler connectionHandler, ConsultantDao consultantDao, ProjectDao projectDao) {
        this.connectionHandler = connectionHandler;
        this.consultantDao = consultantDao;
        this.projectDao = projectDao;
    }

    /* ADD CONSULTANT TO PROJECT */
    /**
     * Adds a consultant to a project.
     * This method inserts a new record into the Work table.
     *
     * @param empNo The EmpNo of the consultant.
     * @param projectNo The ProjectNo of the project.
     * @param workHours The number of hours worked by the consultant on the project.
     * @throws DaoException If there is an error adding the consultant to the project.
     */
    public void addConsultantToProject(String empNo, String projectNo, int workHours) {
        String query = "INSERT INTO Work (ConsultantId, ProjectId, WorkHours) " +
                       "VALUES((SELECT ConsultantId FROM Consultant WHERE EmpNo = ?), " +
                       "(SELECT ProjectId FROM Project WHERE ProjectNo = ?), ?)";
        
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, empNo);
            statement.setString(2, projectNo);
            statement.setInt(3, workHours);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error adding consultant to project.", e);
        }
    }

    /* LIST ALL CONSULTANTS WORKING ON A PROJECT */
    /**
     * Lists all consultants working on a certain project.
     * This method retrieves consultant details based on the project number.
     *
     * @param projectNo The ProjectNo of the project.
     * @return A list of Consultant objects working on the project.
     * @throws DaoException If there is an error retrieving the consultants.
     */
    public List<Consultant> listConsultantsByProject(String projectNo) {
        String query = "SELECT c.EmpNo, c.EmpTitle, c.EmpFirstName, c.EmpLastName " +
                       "FROM Consultant c " +
                       "JOIN Work w ON c.ConsultantId = w.ConsultantId " +
                       "WHERE w.ProjectNo = ?";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, projectNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    consultants.add(consultantDao.mapToConsultant(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error listing consultants for project: " + projectNo, e);
        }

        return consultants;
    }

    /* DISPLAY TOTAL WORKED HOURS FOR A CONSULTANT */
    /**
     * Displays the total worked hours for a certain consultant.
     * This method retrieves the sum of hours worked by the consultant across all projects.
     *
     * @param empNo The EmpNo of the consultant.
     * @return The total worked hours.
     * @throws DaoException If there is an error retrieving the total worked hours.
     */
    public int getTotalWorkedHoursForConsultant(String empNo) {
        String query = "SELECT SUM(w.WorkHours) AS TotalWorkedHours " +
                       "FROM Consultant c " +
                       "JOIN Work w ON w.ConsultantId = c.ConsultantId " +
                       "WHERE c.EmpNo = ?";
        int totalWorkedHours = 0;

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, empNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalWorkedHours = resultSet.getInt("TotalWorkedHours");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching total worked hours for consultant: " + empNo, e);
        }

        return totalWorkedHours;
    }

    /* RETRIEVE CONSULTANTS WORKING ON THREE PROJECTS OR LESS */
    /**
     * Retrieves information on all consultants who work in three projects or less.
     * This method retrieves consultant details based on the number of projects they are involved in.
     *
     * @return A list of Consultant objects.
     * @throws DaoException If there is an error retrieving the consultants.
     */
    public List<Consultant> getConsultantsWithThreeProjectsOrLess() {
        String query = "SELECT c.EmpNo, c.EmpTitle, c.EmpFirstName, c.EmpLastName " +
                       "FROM Consultant c " +
                       "LEFT JOIN Work w ON c.ConsultantId = w.ConsultantId " +
                       "GROUP BY c.EmpNo, c.EmpFirstName, c.EmpLastName, c.EmpTitle " +
                       "HAVING COUNT(DISTINCT w.ProjectId) <= 3";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
             
            while (resultSet.next()) {
                consultants.add(consultantDao.mapToConsultant(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving consultants with three projects or less.", e);
        }

        return consultants;
    }

    /* RETRIEVE PROJECTS INVOLVING EVERY CONSULTANT */
    /**
     * Retrieves information on projects that involve every consultant.
     * This method retrieves project details based on the involvement of all consultants.
     *
     * @return A list of Project objects.
     * @throws DaoException If there is an error retrieving the projects.
     */
    public List<Project> getProjectsInvolvingAllConsultants() {
        String query = "SELECT p.ProjectStatus, p.ProjectNo, p.ProjectName, " +
                       "p.ProjectStartDate, p.ProjectEndDate " +
                       "FROM Project p " +
                       "JOIN Work w ON w.ProjectId = p.ProjectId " +
                       "GROUP BY p.ProjectNo, p.ProjectName, p.ProjectStartDate, " +
                       "p.ProjectEndDate, p.ProjectStatus " +
                       "HAVING COUNT(w.ConsultantId) = (SELECT COUNT(ConsultantId) FROM Consultant)";
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
             
            while (resultSet.next()) {
                projects.add(projectDao.mapToProject(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving projects involving all consultants.", e);
        }

        return projects;
    }

    /* DISPLAY TOTAL NUMBER OF HOURS WORKED BY ALL CONSULTANTS */
    /**
     * Displays the total number of hours worked by all consultants across all projects.
     * This method retrieves the sum of hours worked by all consultants.
     *
     * @return The total hours worked.
     * @throws DaoException If there is an error retrieving the total hours.
     */
    public int getTotalHoursWorked() {
        String query = "SELECT SUM(WorkHours) AS TotHoursWorked FROM Work";
        int totalHoursWorked = 0;

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
             
            if (resultSet.next()) {
                totalHoursWorked = resultSet.getInt("TotHoursWorked");
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching total hours worked by all consultants.", e);
        }

        return totalHoursWorked;
    }

    /* UPDATE HOURS OF A CONSULTANT ON A PROJECT */
    /**
     * Updates the hours of a consultant on a certain project.
     * This method updates the work hours for a consultant on a specific project.
     *
     * @param projectNo The ProjectNo of the project.
     * @param empNo The EmpNo of the consultant.
     * @param workHours The new number of work hours.
     * @throws DaoException If there is an error updating the work hours.
     */
    public void updateWorkHours(String projectNo, String empNo, int workHours) {
        String query = "UPDATE Work SET WorkHours = ? " +
                       "WHERE ProjectId = (SELECT ProjectId FROM Project WHERE ProjectNo = ?) " +
                       "AND ConsultantId = (SELECT ConsultantId FROM Consultant WHERE EmpNo = ?)";
        
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setInt(1, workHours);
            statement.setString(2, projectNo);
            statement.setString(3, empNo);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating work hours for consultant: " + empNo + " on project: " + projectNo, e);
        }
    }

    /* RETRIEVE HARDEST WORKING CONSULTANT */
    /**
     * Retrieves information on the hardest working consultant.
     * This method retrieves the consultant with the highest average work hours per week.
     *
     * @return The Consultant object representing the hardest working consultant.
     * @throws DaoException If there is an error retrieving the consultant.
     */
    public Consultant getHardestWorkingConsultant() {
        String query = "SELECT TOP 1 C.EmpNo, C.EmpFirstName, C.EmpLastName, " +
                       "SUM(W.WorkHours) AS TotalWorkHours, " +
                       "DATEDIFF(DAY, C.EmpStartDate, GETDATE()) AS DaysEmployed, " +
                       "CASE WHEN DATEDIFF(DAY, C.EmpStartDate, GETDATE()) > 0 THEN " +
                       "SUM(W.WorkHours) / (DATEDIFF(DAY, C.EmpStartDate, GETDATE()) / 7.0) " +
                       "ELSE 0 END AS AverageWorkHoursPerWeek " +
                       "FROM Consultant C " +
                       "LEFT JOIN Work W ON C.ConsultantId = W.ConsultantId " +
                       "GROUP BY C.ConsultantId, C.EmpNo, C.EmpFirstName, C.EmpLastName, C.EmpStartDate " +
                       "ORDER BY AverageWorkHoursPerWeek DESC LIMIT 1";
        Consultant consultant = null;

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
             
            if (resultSet.next()) {
                consultant = consultantDao.mapToConsultant(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving the hardest working consultant.", e);
        }

        return consultant;
    }
}