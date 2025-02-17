package se.lu.ics.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.lu.ics.models.Milestone;

public class MilestoneDao {

    private ConnectionHandler connectionHandler;

    public MilestoneDao(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    /* SAVE MILESTONE */
    /**
     * Saves a new milestone to the database.
     * This method inserts a new milestone record (MilestoneNo, ProjectId,
     * MilestoneDate, MilestoneName) into the database.
     *
     * @param milestone The Milestone object containing the data to be saved.
     * @throws DaoException If there is an error saving the milestone.
     */
    public void save(Milestone milestone) {
        String query = "INSERT INTO Milestone (MilestoneNo, ProjectId, MilestoneDate, MilestoneName) VALUES (?, (SELECT ProjectId FROM Project WHERE ProjectNo = ?), ?, ?)";
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Set milestone data into the prepared statement
            statement.setString(1, milestone.getMilestoneNo());
            statement.setString(2, milestone.getProjectNo());
    
            // Check if milestone date is not null
            if (milestone.getMilestoneDate() != null) {
                statement.setDate(3, Date.valueOf(milestone.getMilestoneDate()));
            } else {
                statement.setNull(3, java.sql.Types.DATE);
            }
    
            statement.setString(4, milestone.getMilestoneName());
    
            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new DaoException("Error: A combination of this MilestoneNo and ProjectNo already exists.", e);
            } else if (e.getErrorCode() == 515) {
                throw new DaoException("Error: Fields MilestoneNo and ProjectNo cannot be empty.", e);
            } else if (e.getErrorCode() == 547) {
                throw new DaoException("Error: MilestoneDate must be after 2022-01-01", e);
            } else if (e.getErrorCode() == 50000) {
                throw new DaoException("Error: MilestoneDate must be before project's end date, and after project's start date", e);
            } else {
                throw new DaoException("Error saving milestone: " + milestone.getMilestoneNo(), e);
            }
        }
    }

    /* FIND MILESTONES BY PROJECT NO */
    /**
     * Retrieves all milestones for a certain project, sorted by date.
     * This method executes a SQL SELECT statement to fetch milestone details and
     * returns them as a list of Milestone objects.
     *
     * @param projectNo The ProjectNo of the project whose milestones are to be
     *                  retrieved.
     * @return A list of Milestone objects.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Milestone> findByProjectNo(String projectNo) {
        String query = "SELECT MilestoneNo, MilestoneName, MilestoneDate FROM Milestone WHERE ProjectId = (SELECT ProjectId FROM Project WHERE ProjectNo =?) ORDER BY MilestoneDate";
        List<Milestone> milestones = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the projectNo parameter in the prepared statement
            statement.setString(1, projectNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Iterate through the result set and map each row to a Milestone object
                while (resultSet.next()) {
                    milestones.add(mapToMilestone(resultSet));
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 515) {
                throw new DaoException("ProjectId is not allowed to contain NULL-values.", e);
            } else {
                throw new DaoException("Error fetching milestones for project: " + projectNo, e);
            }
        }

        return milestones;
    }

    /* GET TOTAL NUMBER OF MILESTONES BY PROJECT NO */
    /**
     * Retrieves the total number of milestones for a certain project.
     * This method executes a SQL SELECT COUNT statement to fetch the total number
     * of milestones and returns the count.
     *
     * @param projectNo The ProjectNo of the project whose total number of
     *                  milestones is to be retrieved.
     * @return The total number of milestones.
     * @throws DaoException If there is an error accessing the database.
     */
    public int getTotalNumberOfMilestones(String projectNo) {
        String query = "SELECT COUNT(MilestoneId) AS TotNbrOfMilestones FROM Milestone WHERE ProjectId = (SELECT ProjectId FROM Project WHERE ProjectNo = ?)";
        int totalNumberOfMilestones = 0;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the projectNo parameter in the prepared statement
            statement.setString(1, projectNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Retrieve the total number of milestones from the result set
                if (resultSet.next()) {
                    totalNumberOfMilestones = resultSet.getInt("TotNbrOfMilestones");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 515) {
                throw new DaoException("ProjectNo is not allowed to contain NULL-values.", e);
            } else {
                throw new DaoException("Error fetching the total number of milestones for project: " + projectNo, e);
            }
        }

        return totalNumberOfMilestones;
    }

    /* DELETE MILESTONE BY PROJECT NO AND MILESTONE NO */
    /**
     * Deletes a milestone from a project.
     * This method deletes the record of the milestone matching the given ProjectNo
     * and MilestoneNo.
     *
     * @param projectNo   The ProjectNo of the project.
     * @param milestoneNo The MilestoneNo of the milestone to be deleted.
     * @throws DaoException If there is an error deleting the milestone.
     */
    public void deleteByProjectNoAndMilestoneNo(String projectNo, String milestoneNo) {
        String query = "DELETE FROM Milestone WHERE ProjectId = (SELECT ProjectId FROM Project WHERE ProjectNo = ?) AND MilestoneNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the projectNo and milestoneNo parameters in the prepared statement
            statement.setString(1, projectNo);
            statement.setString(2, milestoneNo);

            // Execute the delete operation
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 515) {
                throw new DaoException("Fields ProjectNo and MilestoneNo cannot be empty.", e);
            } else {
                throw new DaoException(
                        "Error deleting milestone with MilestoneNo: " + milestoneNo + " for project: " + projectNo, e);
            }
        }
    }
    /*FIND ALL MILESTONE NUMBERS */
    /**
     * Retrieves all milestone numbers from the Milestone table.
     *
     * @return a list of milestone numbers.
     * @throws DaoException if there is an error fetching the milestone numbers from
     *                      the database.
     */
    public List<String> findAllMilestoneNumbers() {
        String query = "SELECT MilestoneNo FROM Milestone";
        List<String> milestoneNumbers = new ArrayList<>();
        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    milestoneNumbers.add(resultSet.getString("MilestoneNo"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching all milestone numbers", e);
        }
        return milestoneNumbers;
    }

    /* MAP ROW IN RESULTSET TO MILESTONE OBJECT */
    /**
     * Maps a row in the ResultSet to a Milestone object.
     * This method is a helper function used to convert the result of a SQL query
     * into a Milestone object.
     *
     * @param resultSet The ResultSet containing the milestone data.
     * @return A Milestone object with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the data in the
     *                      ResultSet.
     */
    protected Milestone mapToMilestone(ResultSet resultSet) throws SQLException {
        String milestoneNo = resultSet.getString("MilestoneNo");
        String milestoneName = resultSet.getString("MilestoneName");
        LocalDate milestoneDate = resultSet.getDate("MilestoneDate") != null 
            ? resultSet.getDate("MilestoneDate").toLocalDate() 
            : null;
    
        // Assuming you want to keep projectNo as null since it's not being fetched here.
        return new Milestone(milestoneNo, null, milestoneName, milestoneDate);
    }
}