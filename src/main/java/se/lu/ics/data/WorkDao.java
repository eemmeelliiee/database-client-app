package se.lu.ics.data;

import java.sql.SQLWarning;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;

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
    public String addConsultantToProject(String empNo, String projectNo, int workHours) {
        String query = "INSERT INTO Work (ConsultantId, ProjectId, WorkHours) " +
                       "VALUES((SELECT ConsultantId FROM Consultant WHERE EmpNo = ?), " +
                       "(SELECT ProjectId FROM Project WHERE ProjectNo = ?), ?)";
        
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, empNo);
            statement.setString(2, projectNo);
            statement.setInt(3, workHours);
            statement.executeUpdate();
    
            // Check for SQL warnings
            SQLWarning warning = statement.getWarnings();
            if (warning != null) {
                StringBuilder warningMessage = new StringBuilder();
                while (warning != null) {
                    warningMessage.append("SQL Warning:\n");
                    warningMessage.append("State  : ").append(warning.getSQLState()).append("\n");
                    warningMessage.append("Message: ").append(warning.getMessage()).append("\n");
                    warningMessage.append("Error  : ").append(warning.getErrorCode()).append("\n");
                    warning = warning.getNextWarning();
                }
                return warningMessage.toString();
            }
        } catch (SQLException e) {
            throw new DaoException("Error adding consultant to project.", e);
        }
        return null; // No warnings
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
        String query = "SELECT c.EmpNo, c.EmpTitle, c.EmpFirstName, c.EmpLastName, c.EmpStartDate " +
                       "FROM Consultant c " +
                       "JOIN Work w ON c.ConsultantId = w.ConsultantId " +
                       "WHERE w.ProjectId = (SELECT ProjectId FROM Project WHERE ProjectNo = ?)";
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
        String query = "SELECT c.EmpNo, c.EmpTitle, c.EmpFirstName, c.EmpLastName, c.EmpStartDate, SUM(w.ProjectId) AS NbrOfProjects " +
                       "FROM Consultant c " +
                       "LEFT JOIN Work w ON c.ConsultantId = w.ConsultantId " +
                       "GROUP BY c.EmpNo, c.EmpFirstName, c.EmpLastName, c.EmpTitle, c.EmpStartDate " +
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

    /* RETRIEVE NUMBER OF PROJECTS FOR CONSULTANT */
    /**
     * Retrieves the number of projects for a specific consultant.
     * This method retrieves the number of projects a consultant is involved in
     * based on their employee number.
     *
     * @param empNo The employee number of the consultant.
     * @return The number of projects the consultant is involved in.
     * @throws DaoException If there is an error retrieving the number of projects.
     */
    public int getNumberOfProjectsForConsultant(String empNo) throws DaoException {
        String query = "SELECT COUNT(DISTINCT w.ProjectId) AS NbrOfProjects " +
                "FROM Consultant c " +
                "LEFT JOIN Work w ON c.ConsultantId = w.ConsultantId " +
                "WHERE c.EmpNo = ?";
        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, empNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("NbrOfProjects");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving number of projects for consultant.", e);
        }
        return 0;
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
     * This method retrieves the consultant with the highest total work hours.
     *
     * @return The Consultant object representing the hardest working consultant.
     * @throws DaoException If there is an error retrieving the consultant.
     */
    public Consultant getHardestWorkingConsultant() throws DaoException {
        String query = "SELECT TOP 1 C.EmpNo, C.EmpTitle, C.EmpFirstName, C.EmpLastName, C.EmpStartDate, SUM(W.WorkHours) AS TotalWorkHours " +
                       "FROM Consultant C " +
                       "LEFT JOIN Work W ON C.ConsultantId = W.ConsultantId " +
                       "GROUP BY C.EmpNo, C.EmpTitle, C.EmpFirstName, C.EmpLastName, C.EmpStartDate " +
                       "ORDER BY TotalWorkHours DESC";

        Consultant consultant = null;

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
             
            if (resultSet.next()) {
                consultant = consultantDao.mapToConsultant(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new DaoException("Error retrieving the hardest working consultant.", e);
        }

        return consultant;
    }

    /* RETRIEVE TOTAL WORK HOURS FOR CONSULTANT */
    /**
     * Retrieves the total work hours for a specific consultant.
     * This method retrieves the total work hours a consultant has logged based on their employee number.
     *
     * @param empNo The employee number of the consultant.
     * @return The total work hours the consultant has logged.
     * @throws DaoException If there is an error retrieving the total work hours.
     */
    public int getTotalWorkHoursForConsultant(String empNo) throws DaoException {
        String query = "SELECT SUM(W.WorkHours) AS TotalWorkHours " +
                       "FROM Consultant C " +
                       "LEFT JOIN Work W ON C.ConsultantId = W.ConsultantId " +
                       "WHERE C.EmpNo = ?";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, empNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalWorkHours");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new DaoException("Error retrieving total work hours for consultant.", e);
        }
        return 0;
    }

    /* DELETE WORK RELATIONSHIP */
    /**
     * Deletes the relationship between a consultant and a project.
     * This method removes the entry from the Work table based on the empNo and
     * project number.
     *
     * @param empNo     The employee number of the consultant.
     * @param projectNo The number of the project.
     * @throws DaoException If there is an error deleting the relationship.
     */
    public void deleteConsultantFromProject(String empNo, String projectNo) {
        String query = "DELETE FROM Work WHERE ConsultantId = (SELECT ConsultantId FROM Consultant WHERE EmpNo = ?) AND ProjectId = (SELECT ProjectId FROM Project WHERE ProjectNo = ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, empNo);
            statement.setString(2, projectNo);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DaoException("No relationship found for EmpNo: " + empNo + " and ProjectNo: " + projectNo);
            }
        } catch (SQLException e) {
            throw new DaoException("Error deleting relationship for EmpNo: " + empNo + " and ProjectNo: " + projectNo,
                    e);
        }
    }
}