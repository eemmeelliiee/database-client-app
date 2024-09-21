package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.lu.ics.models.Consultant;

public class ConsultantDao {

    private ConnectionHandler connectionHandler;

    public ConsultantDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    /**
     * Retrieves all consultants from the database.
     * This method executes a SQL SELECT statement to fetch consultant details
     * (EmpNo, EmpFirstName, EmpLastName, EmpTitle, EmpStartDate) and returns them as a list of Consultant objects.
     *
     * @return A list of Consultant objects.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Consultant> findAll() {
        String query = "SELECT EmpNo, EmpFirstName, EmpLastName, EmpTitle, EmpStartDate FROM Consultant;";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and map each row to a Consultant object
            while (resultSet.next()) {
                consultants.add(mapToConsultant(resultSet));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching all consultants.", e);
        }

        return consultants;
    }

    /**
     * Saves a new consultant to the database.
     * This method inserts a new consultant record (EmpNo, EmpFirstName, EmpLastName, EmpTitle, EmpStartDate) into
     * the database.
     *
     * @param consultant The Consultant object containing the data to be saved.
     * @throws DaoException If there is an error saving the consultant (e.g., if the
     *                      EmpNo already exists).
     */
    public void save(Consultant consultant) {
        String query = "INSERT INTO Consultant(EmpNo, EmpFirstName, EmpLastName, EmpTitle, EmpStartDate) VALUES (?,?,?,?,?);";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set consultant data into the prepared statement
            statement.setString(1, consultant.getEmpNo());
            statement.setString(2, consultant.getEmpFirstName());
            statement.setString(3, consultant.getEmpLastName());
            statement.setString(4, consultant.getEmpTitle());
            statement.setDate(5, Date.valueOf(consultant.getEmpStartDate()));

            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Check for unique constraint violation (SQL Server error code 2627)
            if (e.getErrorCode() == 2627) {
                throw new DaoException("A consultant with this EmpNo already exists.", e);
            } else {
                throw new DaoException("Error saving consultant: " + consultant.getEmpNo(), e);
            }
        }
    }

    /**
     * Updates an existing consultants's details in the database.
     * This method updates the consultants's information based on the provided Consultant
     * object.
     *
     * @param consultant The Consultant object containing the updated data.
     * @throws DaoException If there is an error updating the consultant's data.
     */
    public void update(Consultant consultant) {
        String query = "UPDATE Consultant SET EmpNo = ?, EmpFirstName = ?, EmpLastName = ?, EmpTitle = ?, EmpStartDate = ? WHERE EmpNo = ?;";

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Set updated consultant data into the prepared statement
            statement.setString(1, consultant.getEmpNo());
            statement.setString(2, consultant.getEmpFirstName());
            statement.setString(3, consultant.getEmpLastName());
            statement.setString(4, consultant.getEmpTitle());
            statement.setDate(5, Date.valueOf(consultant.getEmpStartDate())); // Convert LocalDate to java.sql.Date
            statement.setString(6, consultant.getEmpNo());
    
            // Execute the update operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Throw a DaoException for any SQL errors
            throw new DaoException("Error updating consultant: " + consultant.getEmpNo(), e);
        }
    }

    /**
     * Deletes a consultant from the database by EmpNo.
     * This method deletes the record of the consultant matching the given Consultant
     * No.
     *
     * @param empNo The EmpNo of the consultant to be deleted.
     * @throws DaoException If there is an error deleting the consultant.
     */
    public void deleteByEConsultantNumber(String empNo) {
        String query = "DELETE FROM Consultant WHERE EmpNo = ?;";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set EmpNo in the prepared statement
            statement.setString(1, empNo);

            // Execute the delete operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Throw a DaoException for SQL errors during deletion
            throw new DaoException("Error deleting consultant with EmpNo: " + empNo, e);
        }
    }

    /**
     * Maps a row in the ResultSet to an Consultant object.
     * This method is a helper function used to convert the result of a SQL query
     * into a Consultant object.
     *
     * @param resultSet The ResultSet containing the consultant data.
     * @return A Consultant object with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the data in the
     *                      ResultSet.
     */
    private Consultant mapToConsultant(ResultSet resultSet) throws SQLException {
        return new Consultant(
                resultSet.getString("EmpNo"),
                resultSet.getString("EmpFirstName"),
                resultSet.getString("EmpLastName"),
                resultSet.getString("EmpTitle"),
                resultSet.getDate("EmpStartDate").toLocalDate());
    }

}