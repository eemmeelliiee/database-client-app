package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetaDataDao {

    private ConnectionHandler connectionHandler;

    public MetaDataDao(ConnectionHandler connectionHandler) throws IOException {
        this.connectionHandler = connectionHandler;
    }

    /**
     * Retrieves the names of all columns in the Consultant table that are not of type INTEGER.
     * This method executes a SQL query against the INFORMATION_SCHEMA to fetch column names
     * that are not of type INTEGER.
     *
     * @return A list of column names that are not of type INTEGER.
     * @throws DaoException  If there is an error accessing the database.
     */
    public List<String> getNonIntegerColumns() {
        String query = "SELECT COLUMN_NAME " +
                       "FROM INFORMATION_SCHEMA.COLUMNS " +
                       "WHERE TABLE_NAME = 'Consultant' AND DATA_TYPE NOT IN ('INT', 'INTEGER', 'BIGINT', 'SMALLINT', 'TINYINT')";
        List<String> nonIntegerColumns = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add column names to the list
            while (resultSet.next()) {
                nonIntegerColumns.add(resultSet.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching non-integer columns from Consultant table.", e);
        }

        return nonIntegerColumns;
    }

    public String[] getTableWithMostRows() {
        String query = "SELECT TOP 1 o.name AS TABLE_NAME, p.rows AS TABLE_ROWS " +
                       "FROM sys.objects o " +
                       "INNER JOIN sys.partitions p ON o.object_id = p.object_id " +
                       "WHERE o.type = 'U' AND p.index_id IN (0, 1) " +  // User tables and heap
                       "ORDER BY p.rows DESC";
    
        String[] result = new String[2];
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            if (resultSet.next()) {
                result[0] = resultSet.getString("TABLE_NAME");
                result[1] = String.valueOf(resultSet.getInt("TABLE_ROWS"));
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching the table with the most rows.", e);
        }
    
        return result;
    }
}
