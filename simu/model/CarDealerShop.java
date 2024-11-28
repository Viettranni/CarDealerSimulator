package simu.model;

import java.util.List;
import java.util.Random;
import java.util.Set;

import simu.model.cartypes.SUV;
import simu.model.cartypes.Van;

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



    // Main method to run the test
    public static void main(String[] args) {
        // Create a CarDealerShop instance
        CarDealerShop shop = new CarDealerShop();

        // Creating a Van object with sample values
        Van van = new Van(0.7, 30000, 4000, 25000, 1.2);
        shop.addCar(van);  // Add van to the shop collection

        // Creating an SUV object with sample values
        SUV suv = new SUV(0.8, 35000, 5000, 28000, 1.3);
        shop.addCar(suv);  // Add SUV to the shop collection

        // Output the register numbers to verify that they were generated
        System.out.println("Van Register Number: " + van.getRegisterNumber());
        System.out.println("SUV Register Number: " + suv.getRegisterNumber());

        String vanregister = van.getRegisterNumber();

        shop.sellCar(vanregister);
    }
}
