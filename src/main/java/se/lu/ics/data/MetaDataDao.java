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

    /* GET NON INTEGER COLUMNS */
    /**
     * Retrieves the names of all columns in the Consultant table that are not of
     * type INTEGER.
     * This method executes a SQL query against the INFORMATION_SCHEMA to fetch
     * column names that are not of type INTEGER.
     *
     * @return A list of column names that are not of type INTEGER.
     * @throws DaoException If there is an error accessing the database.
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

    /* GET NAME AND ROW COUNT FOR TABLE WITH MOST ROWS */
    /**
     * Retrieves the name and row count of the table with the most rows.
     * This method executes a SQL query to fetch the table name and row count
     * of the table with the most rows.
     *
     * @return An array containing the table name and row count.
     * @throws DaoException If there is an error accessing the database.
     */
    public String[] getTableWithMostRows() {
        String query = "SELECT TOP 1 o.name AS TABLE_NAME, p.rows AS TABLE_ROWS " +
                "FROM sys.objects o " +
                "INNER JOIN sys.partitions p ON o.object_id = p.object_id " +
                "WHERE o.type = 'U' AND p.index_id IN (0, 1) " + // User tables and heap
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

    /* GET NAMES OF ALL COLUMNS */
    /**
     * Retrieves the names of all columns in the database.
     * This method executes a SQL query to fetch the names of all columns
     * in the database.
     *
     * @return A list of column names.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<String> getAllColumns() {
        String query = "SELECT DISTINCT sys.columns.name AS ColumnName " +
                "FROM sys.columns " +
                "JOIN sys.tables ON sys.columns.object_id = sys.tables.object_id";
        List<String> allColumns = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                allColumns.add(resultSet.getString("ColumnName"));
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching all columns from the database.", e);
        }

        return allColumns;
    }

    /* GET NAMES OF ALL PRIMARY KEY CONSTRAINTS */
    /**
     * Retrieves the names of all primary key constraints in the database.
     * This method executes a SQL query to fetch the names of all primary key
     * constraints in the database.
     *
     * @return A list of primary key constraint names.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<String> getPrimaryKeyConstraints() {
        String query = "SELECT CONSTRAINT_NAME " +
                "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS " +
                "WHERE CONSTRAINT_TYPE = 'PRIMARY KEY'";
        List<String> primaryKeyConstraints = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add constraint names to the list
            while (resultSet.next()) {
                primaryKeyConstraints.add(resultSet.getString("CONSTRAINT_NAME"));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching primary key constraints.", e);
        }

        return primaryKeyConstraints;
    }

    /* GET NAMES OF ALL CHECK CONSTRAINTS */
    /**
     * Retrieves the names of all check constraints in the database.
     * This method executes a SQL query to fetch the names of all check
     * constraints in the database.
     *
     * @return A list of check constraint names.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<String> getCheckConstraints() {
        String query = "SELECT OBJECT_NAME(object_id) AS CheckConstraintName " +
                "FROM sys.check_constraints";
        List<String> checkConstraints = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add check constraint names to the list
            while (resultSet.next()) {
                checkConstraints.add(resultSet.getString("CheckConstraintName"));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching check constraints from the database.", e);
        }

        return checkConstraints;
    }
}