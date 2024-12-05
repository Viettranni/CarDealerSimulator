package simu.dao;

import simu.datasource.MariaDbConnection;
import simu.model.Car; // Replace with the appropriate car class.

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Data Access Object (DAO) class for handling operations related to cars and database tables.
 */

public class CarsDao {
    private String errorMessage;

    /**
     * Gets the error message from the last operation, if any.
     *
     * @return A string containing the error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    /**
     * Retrieves all table names in the current database.
     *
     * @return An ArrayList of strings representing the table names.
     */
    public ArrayList<String[]> getCarsToBeAdded(String tableName) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return null;
        }

        String sql = "SELECT * FROM " + tableName;
        ArrayList<String[]> carsToBeAdded = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Adjust indexes based on your table structure
                int id = rs.getInt("id");
                int amount = rs.getInt("amount");
                String carType = rs.getString("car_type");
                String fuelType = rs.getString("fuel_type");
                double meanPrice = rs.getDouble("mean_price");
                double priceVariance = rs.getDouble("price_variance");
                double basePrice = rs.getDouble("base_price");

                String amountStr = String.valueOf(amount);
                String meanPriceStr = String.valueOf(meanPrice);
                String priceVarianceStr = String.valueOf(priceVariance);
                String basePriceStr = String.valueOf(basePrice);

                // Add the data to the list as a String[]
                carsToBeAdded.add(new String[]{amountStr, carType, fuelType, meanPriceStr, priceVarianceStr, basePriceStr});
            }
        } catch (SQLException e) {
            errorMessage = "Error fetching entities from the database: ";
            e.printStackTrace();
        }
        return carsToBeAdded;
    }

    /**
     * Retrieves all table names in the current database.
     *
     * @return An ArrayList of strings representing the table names.
     */
    public ArrayList<String> getAllTableNames() {
        Connection conn = MariaDbConnection.getConnection();
        ArrayList<String> tableNames = new ArrayList<>();

        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return null;
        }

        // Query to get all table names from the current database
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        } catch (SQLException e) {
            errorMessage = "Error fetching table names from the database: ";
            e.printStackTrace();
        }

        return tableNames;
    }

    /**
     * Creates a new table with predefined columns in the specified order.
     *
     * @param tableName The name of the new table to be created.
     */
    public void createTable(String tableName) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return;
        }

        String sql = "CREATE TABLE " + tableName + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "amount INT(10) NOT NULL, " +
                "car_type VARCHAR(10) NOT NULL, " +
                "fuel_type VARCHAR(10) NOT NULL, " +
                "mean_price DECIMAL(10,4) NOT NULL, " +
                "price_variance DECIMAL(10,4) NOT NULL, " +
                "base_price DECIMAL(10,4)" +
                ");";

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table " + tableName + " created successfully.");
        } catch (SQLException e) {
            errorMessage = "Error creating table " + tableName + ": " + e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * Adds multiple rows to a table using data from an ArrayList of String arrays.
     *
     * @param tableName The name of the table to which rows will be added.
     * @param data      An ArrayList of String arrays where each array represents a row.
     *                  The order of values must be: amount, car_type, fuel_type, mean_price, price_variance, base_price.
     */
    public void addCarsToTable(String tableName, ArrayList<String[]> data) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return;
        }

        String sql = "INSERT INTO " + tableName + " (amount, car_type, fuel_type, mean_price, price_variance, base_price) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (String[] row : data) {
                if (row.length > 5) {
                    // Parse values from the String array and set them into the prepared statement
                    ps.setInt(1, Integer.parseInt(row[0])); // amount
                    ps.setString(2, row[1]);               // car_type
                    ps.setString(3, row[2]);               // fuel_type
                    ps.setDouble(4, Double.parseDouble(row[3])); // mean_price
                    ps.setDouble(5, Double.parseDouble(row[4])); // price_variance
                    ps.setDouble(6, Double.parseDouble(row[5])); // base_price

                    ps.addBatch(); // Add the row to the batch
                } else {
                    // Parse values from the String array and set them into the prepared statement
                    ps.setInt(1, Integer.parseInt(row[0])); // amount
                    ps.setString(2, row[1]);               // car_type
                    ps.setString(3, row[2]);               // fuel_type
                    ps.setDouble(4, Double.parseDouble(row[3])); // mean_price
                    ps.setDouble(5, Double.parseDouble(row[4])); // price_variance

                    ps.addBatch(); // Add the row to the batch
                }
            }

            // Execute the batch of insertions
            ps.executeBatch();
            System.out.println("Data added successfully to table " + tableName);
        } catch (SQLException e) {
            errorMessage = "Error adding cars to the database: " + e.getMessage();
            e.printStackTrace();
        } catch (NumberFormatException e) {
            errorMessage = "Error parsing number values in data: " + e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specific row from a table by ID.
     *
     * @param tableName The name of the table.
     * @param id        The ID of the row to delete.
     */
    public void deleteRowById(String tableName, int id) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return;
        }

        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Row with ID " + id + " deleted successfully from " + tableName);
            } else {
                System.out.println("No row with ID " + id + " found in " + tableName);
            }
        } catch (SQLException e) {
            errorMessage = "Error deleting row from the database: " + e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * Deletes an entire table.
     *
     * @param tableName The name of the table to delete.
     */
    public void deleteTable(String tableName) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return;
        }

        String sql = "DROP TABLE IF EXISTS " + tableName;

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table " + tableName + " deleted successfully.");
        } catch (SQLException e) {
            errorMessage = "Error deleting table: " + e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * Modifies a specific row in a table by ID.
     *
     * @param tableName  The name of the table.
     * @param id         The ID of the row to modify.
     * @param newValues  A String array with new values in the order:
     *                   amount, car_type, fuel_type, mean_price, price_variance, base_price.
     */
    public void modifyRowById(String tableName, int id, String[] newValues) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return;
        }

        String sql = "UPDATE " + tableName + " SET amount = ?, car_type = ?, fuel_type = ?, " +
                "mean_price = ?, price_variance = ?, base_price = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(newValues[0])); // amount
            ps.setString(2, newValues[1]);               // car_type
            ps.setString(3, newValues[2]);               // fuel_type
            ps.setDouble(4, Double.parseDouble(newValues[3])); // mean_price
            ps.setDouble(5, Double.parseDouble(newValues[4])); // price_variance
            ps.setDouble(6, Double.parseDouble(newValues[5])); // base_price
            ps.setInt(7, id);                            // id

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Row with ID " + id + " updated successfully in " + tableName);
            } else {
                System.out.println("No row with ID " + id + " found in " + tableName);
            }
        } catch (SQLException e) {
            errorMessage = "Error updating row in the database: " + e.getMessage();
            e.printStackTrace();
        } catch (NumberFormatException e) {
            errorMessage = "Error parsing number values in new data: " + e.getMessage();
            e.printStackTrace();
        }
    }
}
