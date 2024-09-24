package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Types;
import se.lu.ics.models.Project;

public class ProjectDao {

    private ConnectionHandler connectionHandler;

    public ProjectDao(ConnectionHandler connectionHandler) throws IOException {
        this.connectionHandler = connectionHandler; // Might cange this based on which view is the main view
    }

    /* FIND ALL PROJECTS */
    /**
     * Retrieves all projects from the database.
     * This method executes a SQL SELECT statement to fetch project details
     * (ProjectStatus, ProjectNo, ProjectName, ProjectStartDate, ProjectEndDate) and
     * returns them
     * as a list of Project objects.
     *
     * @return A list of Project objects.
     * 
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Project> findAll() {
        String query = "SELECT ProjectStatus AS Status, ProjectNo, ProjectName AS Name, ProjectStartDate AS StartDate, ProjectEndDate AS EndDate FROM Project ORDER BY ProjectStatus DESC";
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and map each row to a Project object
            while (resultSet.next()) {
                projects.add(mapToProject(resultSet));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching all projects.", e);
        }

        return projects;
    }

    /* FIND PROJECT BY ProjectNo */
    /**
     * Retrieves a project from the database by ProjectNo.
     * This method executes a SQL SELECT statement to fetch project details
     * (ProjectStatus, ProjectNo, ProjectName, ProjectStartDate, ProjectEndDate) and
     * returns the
     * Project object.
     *
     * @param projectNo The ProjectNo of the project to be retrieved.
     * 
     * @return The Project object if found, otherwise null.
     * 
     * @throws DaoException If there is an error accessing the database.
     */
    public Project findByProjectNo(String projectNo) {
        String query = "SELECT ProjectStatus AS Status, ProjectNo, ProjectName AS Name, ProjectStartDate AS StartDate, ProjectEndDate AS EndDate FROM Project WHERE ProjectNo = ?";
        Project project = null;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the projectNo parameter in the prepared statement
            statement.setString(1, projectNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if a result is returned and map it to a Project object
                if (resultSet.next()) {
                    project = mapToProject(resultSet);
                }
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching project with ProjectNo: " + projectNo, e);
        }

        return project;
    }

    public List<String> findAllProjectNos() {
        String query = "SELECT ProjectNo FROM Project";
        List<String> projectNos = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add each EmpNo to the list
            while (resultSet.next()) {
                projectNos.add(resultSet.getString("ProjectNo"));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching all ProjectNos.", e);
        }

        return projectNos;
    }

    /* SAVE PROJECT */
    /**
     * Saves a new project to the database.
     * This method inserts a new project record (ProjectNo, ProjectName,
     * ProjectStartDate, ProjectEndDate) into
     * the database.
     *
     * @param project The Project object containing the data to be saved.
     * 
     * @throws DaoException If there is an error saving the project (e.g., if the
     *                      ProjectNo already exists).
     */
    public void save(Project project) {
        String insertQuery = "INSERT INTO Project (ProjectNo, ProjectName, ProjectStartDate, ProjectEndDate) VALUES (?,?,?,?)";

        try (Connection connection = connectionHandler.getConnection()) {
            // Insert project details
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, project.getProjectNo());
                insertStmt.setString(2, project.getProjectName());
                // Check if projectStartDate is null and handle it appropriately
                if (project.getProjectStartDate() != null) {
                    insertStmt.setDate(3, Date.valueOf(project.getProjectStartDate()));
                } else {
                    insertStmt.setNull(3, Types.DATE);
                }

                // Check if projectEndDate is null and handle it appropriately
                if (project.getProjectEndDate() != null) {
                    insertStmt.setDate(4, Date.valueOf(project.getProjectEndDate()));
                } else {
                    insertStmt.setNull(4, Types.DATE);
                }
                // Execute the insert operation
                insertStmt.executeUpdate();
            }
            setProjectStatus(connection, project); // Updates projectStatus attribute to calculated attribute
                                                   // ProjectStatus in Project table
        } catch (SQLException e) {
            // Check for unique constraint violation (SQL Server error code 2627)
            if (e.getErrorCode() == 2627) {
                throw new DaoException("A project with this ProjectNo or ProjectName already exists.", e);
            } else if (e.getErrorCode() == 515) {
                throw new DaoException("Fields ProjectNo and ProjectName cannot be empty.", e);
            } else if (e.getErrorCode() == 547) {
                throw new DaoException("End date must be after start date", e);
            } else {
                throw new DaoException("Error saving project: " + project.getProjectNo(), e);
            }
        } catch (Exception e) {
            throw new DaoException("Error saving project: " + project.getProjectNo());
        }
    }

    /* UPDATE PROJECT */
    /**
     * Updates an existing project's details in the database.
     * This method updates the project's information based on the provided
     * Project object.
     *
     * @param updatedproject The Project object containing the updated data.
     * @param oldProjectNo   The original ProjectNo used to identify the record to
     *                       be updated.
     * 
     * @throws DaoException If there is an error updating the project's data.
     */
    public void update(Project updatedproject, String oldProjectNo) {
        String updateQuery = "UPDATE Project SET ProjectNo = ?, ProjectName = ?, ProjectStartDate = ?, ProjectEndDate = ? WHERE ProjectNo = ?";

        try (Connection connection = connectionHandler.getConnection()) {
            // Update project details
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, updatedproject.getProjectNo());
                updateStmt.setString(2, updatedproject.getProjectName());
                updateStmt.setDate(3, Date.valueOf(updatedproject.getProjectStartDate()));
                updateStmt.setDate(4, Date.valueOf(updatedproject.getProjectEndDate()));
                updateStmt.setString(5, oldProjectNo); // In case ProjectNo is updated

                // Execute the update operation
                updateStmt.executeUpdate();
            }
            setProjectStatus(connection, updatedproject); // Updates projectStatus attribute to calculated attribute
            // ProjectStatus in Project table
        } catch (SQLException e) {
            // Check for unique constraint violation (SQL Server error code 2627)
            if (e.getErrorCode() == 2627) {
                throw new DaoException("A project with this ProjectNo or ProjectName already exists.", e);
            } else if (e.getErrorCode() == 515) {
                throw new DaoException("Fields ProjectNo and ProjectName cannot be empty.");
            } else if (e.getErrorCode() == 547) {
                throw new DaoException(
                        "The start date is after the end date. Choose a date that starts before the end date.");
            } else {
                throw new DaoException("Error saving project: " + updatedproject.getProjectNo(), e);
            }
        } catch (Exception e) {
            throw new DaoException("Error saving project: " + updatedproject.getProjectNo());
        }
    }

    /* HELPER METHOD: UPDATE ATTRIBUTE projectStatus FOR PROJECT */
    /**
     * Updates the ProjectStatus attribute of an existing project in the database.
     * This method retrieves the current ProjectStatus from the database and updates
     * the provided Project object with this status.
     *
     * @param connection The database connection to be used.
     * @param project    The Project object to be updated with the current status.
     * 
     * @throws DaoException If there is an error updating the project's status.
     */
    public void setProjectStatus(Connection connection, Project project) throws DaoException {
        String selectStatusQuery = "SELECT ProjectStatus AS Status FROM Project WHERE ProjectNo = ?";
        // Retrieve the ProjectStatus for the updated project
        try (PreparedStatement selectStmt = connection.prepareStatement(selectStatusQuery)) {
            selectStmt.setString(1, project.getProjectNo());

            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    String projectStatus = resultSet.getString("Status");
                    project.setProjectStatus(projectStatus); // Set the ProjectStatus in the Project object
                }
            }
        } catch (SQLException e) {
            // Handle the SQLException and rethrow as a custom DaoException
            throw new DaoException("Error updating project status for ProjectNo: " + project.getProjectNo(), e);
        }
    }

    /* DELETE PROJECT */
    /**
     * Deletes a project from the database by ProjectNo.
     * This method deletes the record of the project matching the given
     * ProjectNo.
     *
     * @param projectNo The ProjectNo of the project to be deleted.
     * 
     * @throws DaoException If there is an error deleting the project.
     */
    public void deleteByProjectNo(String projectNo) {
        String query = "DELETE FROM Project WHERE ProjectNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set ProjectNo in the prepared statement
            statement.setString(1, projectNo);

            // Execute the delete operation and get the number of rows affected
            int rowsAffected = statement.executeUpdate();

            // Check if a project was deleted
            if (rowsAffected == 0) {
                throw new DaoException("No project found with ProjectNo: " + projectNo);
            }
        } catch (SQLException e) {
            // Throw a DaoException for SQL errors during deletion
            throw new DaoException("Error deleting project with ProjectNo: " + projectNo, e);
        }
    }

    /* HELPER METHOD: MAP ROW IN RESULTSET TO PROJECT OJBECT */
    /**
     * Maps a row in the ResultSet to a Project object.
     * This method is a helper function used to convert the result of a SQL query
     * into a Project object.
     *
     * @param resultSet The ResultSet containing the project data.
     * @return A Project object with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the data in the
     *                      ResultSet.
     */
    protected Project mapToProject(ResultSet resultSet) throws SQLException {
        return new Project(
                resultSet.getString("ProjectNo"),
                resultSet.getString("Name"),
                resultSet.getDate("StartDate") != null ? resultSet.getDate("StartDate").toLocalDate() : null,
                resultSet.getDate("EndDate") != null ? resultSet.getDate("EndDate").toLocalDate() : null,
                resultSet.getString("Status") != null ? resultSet.getString("Status") : null);
    }

}