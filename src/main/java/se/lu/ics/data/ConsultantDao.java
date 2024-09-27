package se.lu.ics.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Types;
import java.time.LocalDate;

import se.lu.ics.models.Consultant;

public class ConsultantDao {

    private ConnectionHandler connectionHandler;

    public ConsultantDao(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler; // Might change this based on which view is the main view
    }

    /* FIND ALL CONSULTANTS */
    /**
     * Retrieves all consultants from the database.
     * This method executes a SQL SELECT statement to fetch consultant details
     * (EmpNo, EmpFirstName, EmpLastName, EmpTitle, EmpStartDate) and returns them
     * as a list of Consultant objects.
     *
     * @return A list of Consultant objects.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Consultant> findAll() {
        String query = "SELECT EmpNo, EmpFirstName AS FirstName, EmpLastName AS LastName, EmpTitle AS Title, EmpStartDate AS StartDate FROM Consultant";
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

    /* FIND ALL EmpNos */
    /**
     * Retrieves all consultants from the database.
     * This method executes a SQL SELECT statement to fetch EmpNo's and returns them
     * as a list of Strings.
     *
     * @return A list of EmpNo Strings.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<String> findAllEmpNos() {
        String query = "SELECT EmpNo FROM Consultant";
        List<String> empNos = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add each EmpNo to the list
            while (resultSet.next()) {
                empNos.add(resultSet.getString("EmpNo"));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching all EmpNos.", e);
        }

        return empNos;
    }

    /* FIND CONSULTANT BY EmpNo */
    /**
     * Retrieves a consultant from the database by EmpNo.
     * This method executes a SQL SELECT statement to fetch consultant details
     * (EmpNo, EmpTitle, EmpFirstName, EmpLastName, EmpStartDate) and returns the
     * Consultant object.
     *
     * @param empNo The EmpNo of the consultant to be retrieved.
     * @return The Consultant object if found, otherwise null.
     * @throws DaoException If there is an error accessing the database.
     */
    public Consultant findByEmpNo(String empNo) {
        String query = "SELECT EmpNo, EmpTitle AS Title, EmpFirstName AS FirstName, EmpLastName AS LastName, EmpStartDate AS StartDate FROM Consultant WHERE EmpNo = ?";
        Consultant consultant = null;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the empNo parameter in the prepared statement
            statement.setString(1, empNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if a result is returned and map it to a Consultant object
                if (resultSet.next()) {
                    consultant = mapToConsultant(resultSet);
                }
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching consultant with EmpNo: " + empNo, e);
        }

        return consultant;
    }

    /* FIND CONSULTANTS BY EmpTitle */
    /**
     * Retrieves consultants from the database by EmpTitle.
     * This method executes a SQL SELECT statement to fetch consultant details
     * (EmpNo, EmpFirstName, EmpLastName, EmpStartDate) and returns a list
     * of Consultant objects.
     *
     * @param empTitle The EmpTitle of the consultants to be retrieved.
     * @return A list of Consultant objects if found, otherwise an empty list.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Consultant> findByEmpTitle(String empTitle) {
        String query = "SELECT EmpNo, EmpTitle AS Title, EmpFirstName AS FirstName, EmpLastName AS LastName, EmpStartDate AS StartDate FROM Consultant WHERE EmpTitle = ?";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the empTitle parameter in the prepared statement
            statement.setString(1, empTitle);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Iterate through the result set and map each row to a Consultant object
                while (resultSet.next()) {
                    consultants.add(mapToConsultant(resultSet));
                }
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching consultants with EmpTitle: " + empTitle, e);
        }
        return consultants;
    }

    /* FIND ALL EmpTitles */
    /**
     * Retrieves all unique EmpTitles from the database.
     * This method executes a SQL SELECT statement to fetch unique EmpTitles and
     * returns them as a list of Strings.
     *
     * @return A list of EmpTitle Strings.
     * @throws DaoException If there is an error accessing the database.
     */

    public List<String> findAllEmpTitles() {
        String query = "SELECT DISTINCT EmpTitle FROM Consultant";
        List<String> empTitles = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add each EmpTitle to the list
            while (resultSet.next()) {
                empTitles.add(resultSet.getString("EmpTitle"));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching all EmpTitles.", e);
        }
        return empTitles;
    }

    /* SAVE CONSULTANT */
    /**
     * Saves a new consultant to the database.
     * This method inserts a new consultant record (EmpNo, EmpFirstName,
     * EmpLastName, EmpTitle, EmpStartDate) into the database.
     *
     * @param consultant The Consultant object containing the data to be saved.
     * @throws DaoException If there is an error saving the consultant (e.g., if the
     *                      EmpNo already exists).
     */
    public void save(Consultant consultant) {
        String query = "INSERT INTO Consultant (EmpNo, EmpFirstName, EmpLastName, EmpTitle, EmpStartDate) VALUES (?,?,?,?,?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set consultant data into the prepared statement
            statement.setString(1, consultant.getEmpNo());
            statement.setString(2, consultant.getEmpFirstName());
            statement.setString(3, consultant.getEmpLastName());
            statement.setString(4, consultant.getEmpTitle());
            // Check if empStartDate is null and handle it appropriately
            if (consultant.getEmpStartDate() != null) {
                statement.setDate(5, Date.valueOf(consultant.getEmpStartDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Check for constraint violations
            if (e.getErrorCode() == 2627) {
                throw new DaoException("Error: A Consultant with this EmpNo already exists.", e);
            } else if (e.getErrorCode() == 515) {
                throw new DaoException("Error: Fields EmpNo, First name, and Last name cannot be empty.", e);
            } else {
                throw new DaoException("Error registering consultant: ", e);
            }
        } 
    }

    /* UPDATE CONSULTANT BY EmpNo */
    /**
     * Updates an existing consultant's details in the database.
     * This method updates the consultant's information based on the provided
     * Consultant object.
     *
     * @param updatedConsultant The Consultant object containing the updated data.
     * @param oldEmpNo          The original EmpNo used to identify the record to be
     *                          updated.
     * @throws DaoException If there is an error updating the consultant's data.
     */
    public void update(Consultant updatedConsultant, String oldEmpNo) {
        String query = "UPDATE Consultant SET EmpNo = ?, EmpFirstName = ?, EmpLastName = ?, EmpTitle = ?, EmpStartDate = ? WHERE EmpNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set updated consultant data into the prepared statement
            statement.setString(1, updatedConsultant.getEmpNo());
            statement.setString(2, updatedConsultant.getEmpFirstName());
            statement.setString(3, updatedConsultant.getEmpLastName());
            statement.setString(4, updatedConsultant.getEmpTitle());
                   // Check if empStartDate is null and handle it appropriately
            if (updatedConsultant.getEmpStartDate() != null) {
                statement.setDate(5, Date.valueOf(updatedConsultant.getEmpStartDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }                                                                  // java.sql.Date

            statement.setString(6, oldEmpNo); // In case EmpNo is updated

            // Execute the update operation
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new DaoException("Error: A consultant with this EmpNo already exists.", e);
            } else if (e.getErrorCode() == 515) {
                throw new DaoException("Error: Fields EmpNo, First name, and Last name cannot be empty.", e);
            } else {
                throw new DaoException("Error updating consultant '" + updatedConsultant.getEmpNo(), e);
            }
        } 
    }

    /* DELETE CONSULTANT BY EmpNo */
    /**
     * Deletes a consultant from the database by EmpNo.
     * This method deletes the record of the consultant matching the given
     * Consultant No.
     *
     * @param empNo The EmpNo of the consultant to be deleted.
     * @throws DaoException If there is an error deleting the consultant.
     */
    public void deleteByEmpNo(String empNo) {
        String query = "DELETE FROM Consultant WHERE EmpNo = ?";

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

    /* RETRIEVE TOTAL NBR OF CONSULTANTS */
    /**
     * Retrieves the total number of consultants in the system.
     * This method executes a SQL SELECT COUNT statement to fetch the total number
     * of consultants and returns the count.
     *
     * @return The total number of consultants.
     * @throws DaoException If there is an error accessing the database.
     */
    public int getTotalNumberOfConsultants() {
        String query = "SELECT COUNT(ConsultantId) AS TotNbrOfConsultants FROM Consultant";
        int totalNumberOfConsultants = 0;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Retrieve the total number of consultants from the result set
            if (resultSet.next()) {
                totalNumberOfConsultants = resultSet.getInt("TotNbrOfConsultants");
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching the total number of consultants.", e);
        }

        return totalNumberOfConsultants;
    }

    /* MAP ROW IN RESULTSET TO CONSULTANT OBJECT */
    /**
     * Maps a row in the ResultSet to a Consultant object.
     * This method is a helper function used to convert the result of a SQL query
     * into a Consultant object.
     *
     * @param resultSet The ResultSet containing the consultant data.
     * @return A Consultant object with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the data in the
     *                      ResultSet.
     */
    protected Consultant mapToConsultant(ResultSet resultSet) throws SQLException {
        // Extracting the basic information from the ResultSet
        String empNo = resultSet.getString("EmpNo");
        String firstName = resultSet.getString("FirstName");
        String lastName = resultSet.getString("LastName");
        String title = resultSet.getString("Title") != null ? resultSet.getString("Title") : null;
        LocalDate startDate = resultSet.getDate("StartDate") != null ? resultSet.getDate("StartDate").toLocalDate() : null;
        
        // Extracting the TotalWorkHours
        double totalWorkHours = 0;
        try {
            totalWorkHours = resultSet.getDouble("TotalWorkHours");
        } catch (SQLException e) {
            // Handle the case where the column does not exist
            if (!e.getMessage().contains("TotalWorkHours")) {
                throw e;
            }
        }
    
        // Creating a Consultant object
        Consultant consultant = new Consultant(empNo, firstName, lastName, title, startDate);
    
        // Setting the total work hours
        consultant.setTotalWorkHours(totalWorkHours);
        
        return consultant;
    }
}
