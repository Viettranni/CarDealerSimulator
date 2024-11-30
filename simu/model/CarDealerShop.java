package simu.model;

import java.util.List;
import java.util.Random;
import java.util.Set;

import simu.model.cartypes.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class CarDealerShop {
    private List<Car> carCollection;
    private List<Car> soldCars;
    private static Set<String> takenRegisterNumbers = new HashSet<>();

    public CarDealerShop() {
        carCollection = new ArrayList<>();
        soldCars = new ArrayList<>();
    }

    // Getters setters

    public List<Car> getCarCollection() {
        return carCollection;
    }

    // METHODS

    // Adding the car to Car Gallery
    public void addCar(Car vehicle) {
        try {
            if (vehicle == null) {
                System.out.println("Error: Cannot add a null vehicle.");
                return;  // exit early if car is null
            }
            carCollection.add(vehicle);
            System.out.println("Vehicle added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Adding car to sold list
    public void addCarToSold(Car vehicle) {
        try {
            if (vehicle == null) {
                System.out.println("Error: Cannot add a null vehicle.");
                return;  // exit early if car is null
            }
            soldCars.add(vehicle);
            System.out.println("Vehicle " + vehicle.getRegisterNumber() + " was SOLD! Added to the SOLD LIST successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // Removing the car from the Car Gallery
    public void sellCar(String registerNumber) {
        try {
            boolean carFound = false;  // Flag to check if car is found
            
            Iterator<Car> iterator = carCollection.iterator();
            while (iterator.hasNext()) {
                Car car = iterator.next();
                if (car.getRegisterNumber().equals(registerNumber)) {  // Using equals() for string comparison
                    addCarToSold(car); // Adding car to the sold list
                    iterator.remove();  // Removes the car safely using iterator
                    System.out.println("Car with registernumber " + registerNumber + " removed from the Gallery successfully!");
                    carFound = true;
                    break;
                }
            }
            
            if (!carFound) {
                System.out.println("Car with registernumber " + registerNumber + " not found in the Gallery.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Generating unique register number for each vehicle object
    public static String generateUniqueRegisterNumber() {
        Random random = new Random();
        String generatedRegisterNumber;

        // Generate unique number
        do {
            String letters = "";
            for (int i = 0; i < 3; i++) {
                char letter = (char) ('A' + random.nextInt(26));
                letters += letter;
            }

            String digits = "";
            for (int i = 0; i < 3; i++) {
                digits += random.nextInt(10);
            }

            generatedRegisterNumber = letters + "-" + digits;

        } while (takenRegisterNumbers.contains(generatedRegisterNumber));

        takenRegisterNumbers.add(generatedRegisterNumber); // Store the unique number
        return generatedRegisterNumber;
    }

    // Check if a register number has already been taken
    public static boolean isRegisterNumberTaken(String registerNumber) {
        return takenRegisterNumbers.contains(registerNumber);
    }

    // REMEMBER TO LATER CHANGE THIS TO FIT THE UI AND CONTROLLER
    public void createCar(int amount, int carType, int fuelType, double meanPrice, double priceVariance) {
        String actualFuelType = null;

        // Set the actual fuel type based on the input fuelType
        switch (fuelType) {
            case 1:
                actualFuelType = "gas";
                break;
            case 2:
                actualFuelType = "hybrid";
                break;
            case 3:
                actualFuelType = "electric";
                break;
            default:
                actualFuelType = "gas"; // Default to "gas" if an invalid fuelType is provided
                break;
        }

        // Loop to create the specified amount of cars
        for (int i = 0; i < amount; i++) {
            Car car = null;  // Create a new car object in each iteration

            // Create the car based on carType
            switch (carType) {
                case 1:
                    car = new SUV(actualFuelType, meanPrice, priceVariance);
                    break;
                case 2:
                    car = new Van(actualFuelType, meanPrice, priceVariance);
                    break;
                case 3:
                    car = new Sedan(actualFuelType, meanPrice, priceVariance);
                    break;
                case 4:
                    car = new Sport(actualFuelType, meanPrice, priceVariance);
                    break;
                case 5:
                    car = new Compact(actualFuelType, meanPrice, priceVariance);
                    break;
                default:
                    System.out.println("Invalid car type");
                    System.out.println("Cartype" + carType);
                    break;
            }

            if (car != null) {
                addCar(car);  // Add the newly created car to the collection
            }
        }
    }




    // Main method to run the test
    public static void main(String[] args) {
        // Create a CarDealerShop instance
        CarDealerShop shop = new CarDealerShop();

        // Creating a Van object with sample values
        Van van = new Van("gas", 20000, 5000);
        shop.addCar(van);  // Add van to the shop collection

        // Creating an SUV object with sample values
        SUV suv = new SUV("electric", 35000, 10000);
        shop.addCar(suv);  // Add SUV to the shop collection

        // Output the register numbers to verify that they were generated
        System.out.println("Van Register Number: " + van.getRegisterNumber());
        System.out.println("SUV Register Number: " + suv.getRegisterNumber());

        String vanregister = van.getRegisterNumber();

        shop.sellCar(vanregister);
    }
}
