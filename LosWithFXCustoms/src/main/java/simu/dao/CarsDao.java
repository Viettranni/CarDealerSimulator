package simu.dao;

import simu.datasource.MariaDbConnection;
import simu.model.Car; // Replace with the appropriate car class.

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarsDao {
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Car> getAllEntities(String tableName) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return null;
        }

        String sql = "SELECT * FROM " + tableName;
        List<Car> entities = new ArrayList<>();

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

                Car car = new Car(carType, fuelType, meanPrice, priceVariance);
                car.setBasePrice(basePrice);
                
                entities.add(car);
            }
        } catch (SQLException e) {
            errorMessage = "Error fetching entities from the database: ";
            e.printStackTrace();
        }
        return entities;
    }

    public Car getCarById(String tableName, int id) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return null;
        }

        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        Car car = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int amount = rs.getInt("amount");
                String carType = rs.getString("car_type");
                String fuelType = rs.getString("fuel_type");
                double meanPrice = rs.getDouble("mean_price");
                double priceVariance = rs.getDouble("price_variance");
                double basePrice = rs.getDouble("base_price");

                car = new Car(carType, fuelType, meanPrice, priceVariance);
                car.setBasePrice(basePrice);
            }
        } catch (SQLException e) {
            errorMessage = "Error fetching car by ID from the database: ";
            e.printStackTrace();
        }

        return car;
    }

    public void addCar(String tableName, Car car) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return;
        }

        String sql = "INSERT INTO " + tableName + " (amount, car_type, fuel_type, mean_price, price_variance, base_price) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setString(2, car.getCarType());
            ps.setString(3, car.getFuelType());
            ps.setDouble(4, car.getMeanPrice());
            ps.setDouble(5, car.getPriceVariance());
            ps.setDouble(6, car.getBasePrice());

            ps.executeUpdate();
        } catch (SQLException e) {
            errorMessage = "Error adding car to the database: ";
            e.printStackTrace();
        }
    }

    public double getMeanPriceByCarTypeAndFuel(String tableName, String carType, String fuelType) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return 0.0;
        }

        String sql = "SELECT mean_price FROM " + tableName + " WHERE car_type = ? AND fuel_type = ?";
        double meanPrice = 0.0;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, carType);
            ps.setString(2, fuelType);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                meanPrice = rs.getDouble("mean_price");
            }
        } catch (SQLException e) {
            errorMessage = "Error fetching mean price from the database: ";
            e.printStackTrace();
        }

        return meanPrice;
    }

    public List<Car> getEntitiesByCarType(String tableName, String carType) {
        Connection conn = MariaDbConnection.getConnection();
        if (conn == null) {
            errorMessage = "There was an error connecting to the database";
            return null;
        }

        String sql = "SELECT * FROM " + tableName + " WHERE car_type = ?";
        List<Car> entities = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, carType);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int amount = rs.getInt("amount");
                String fuelType = rs.getString("fuel_type");
                double meanPrice = rs.getDouble("mean_price");
                double priceVariance = rs.getDouble("price_variance");
                double basePrice = rs.getDouble("base_price");

                Car car = new Car(carType, fuelType, meanPrice, priceVariance);
                car.setBasePrice(basePrice);
                entities.add(car);
            }
        } catch (SQLException e) {
            errorMessage = "Error fetching entities by car type from the database: ";
            e.printStackTrace();
        }

        return entities;
    }

    public ArrayList<String[]> populateCarsToBeAdded(String tableName) {
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

}
