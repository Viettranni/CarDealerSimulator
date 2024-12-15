package simu.model;

import java.util.*;

import simu.model.cartypes.*;

/**
 * Manages the car dealership operations, including inventory, sales, and customer interactions.
 */
public class CarDealerShop {
    private List<Car> carCollection;
    private List<Car> soldCars;
    private static Set<String> takenRegisterNumbers = new HashSet<>();
    private static double meanCarSalesProbability;
    private LinkedList<Customer> customerAtTheDealership;

    public CarDealerShop() {
        carCollection = new ArrayList<>();
        soldCars = new ArrayList<>();
        customerAtTheDealership = new LinkedList<>();
        meanCarSalesProbability = 0.0;
    }


    /**
     * Adds a car to the car collection (gallery).
     * Validates the car object before adding it.
     *
     * @param vehicle The car to be added to the gallery.
     */
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


    /**
     * Adds a car to the list of sold cars.
     * Validates the car object before adding it.
     *
     * @param vehicle The car to be added to the sold list.
     */
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


    /**
     * Sells a car by its register number.
     * Removes the car from the gallery and adds it to the sold list.
     *
     * @param registerNumber The register number of the car to be sold.
     */
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

    /**
     * Generates a unique register number for a car.
     * Ensures that the generated number is not already taken.
     *
     * @return A unique register number.
     */
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

    /**
     * Sets the mean car sales probability based on the car collection.
     * Calculates the average base probability of all cars in the collection.
     */
    public void setMeanCarSalesProbability() {
        double total = 0;
        double sum = 0;
        for (Car car : carCollection) {
            total++;
            sum += car.getBaseProb();
        }
        meanCarSalesProbability = sum / total;
    }


    /**
     * Creates a specified number of cars and adds them to the car collection.
     * Configures each car based on its type, fuel type, price, and variance.
     *
     * @param amount       The number of cars to create.
     * @param carType      The type of the car (e.g., SUV, Sedan).
     * @param fuelType     The type of fuel the car uses.
     * @param meanPrice    The average price of the car.
     * @param priceVariance The variance in the price of the car.
     */
    public void createCar(int amount, String carType, String fuelType, double meanPrice, double priceVariance) {
        String actualFuelType = fuelType.toLowerCase();
        // Loop to create the specified amount of cars
        for (int i = 0; i < amount; i++) {
            Car car = null;  // Create a new car object in each iteration

            // Create the car based on carType
            switch (carType.toLowerCase()) {
                case "suv":
                    car = new SUV(actualFuelType, meanPrice, priceVariance);
                    break;
                case "van":
                    car = new Van(actualFuelType, meanPrice, priceVariance);
                    break;
                case "sedan":
                    car = new Sedan(actualFuelType, meanPrice, priceVariance);
                    break;
                case "sport":
                    car = new Sport(actualFuelType, meanPrice, priceVariance);
                    break;
                case "compact":
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


    /**
     * Creates a specified number of cars and adds them to the car collection.
     * Configures each car based on its type, fuel type, price, variance, <strong>and base price</strong>.
     *
     * @param amount       The number of cars to create.
     * @param carType      The type of the car (e.g., SUV, Sedan).
     * @param fuelType     The type of fuel the car uses.
     * @param meanPrice    The average price of the car.
     * @param priceVariance The variance in the price of the car.
     * @param basePrice     The base price of the car.
     */
    public void createCar(int amount, String carType, String fuelType, double meanPrice, double priceVariance, double basePrice) {
        String actualFuelType = fuelType.toLowerCase();
        // Loop to create the specified amount of cars
        for (int i = 0; i < amount; i++) {
            Car car = null;  // Create a new car object in each iteration

            // Create the car based on carType
            switch (carType.toLowerCase()) {
                case "suv":
                    car = new SUV(actualFuelType, meanPrice, priceVariance, basePrice);
                    break;
                case "van":
                    car = new Van(actualFuelType, meanPrice, priceVariance, basePrice);
                    break;
                case "sedan":
                    car = new Sedan(actualFuelType, meanPrice, priceVariance, basePrice);
                    break;
                case "sport":
                    car = new Sport(actualFuelType, meanPrice, priceVariance, basePrice);
                    break;
                case "compact":
                    car = new Compact(actualFuelType, meanPrice, priceVariance, basePrice);
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


    /**
     * @hidden
     */
    public double getMeanCarSalesProbability() {
        return meanCarSalesProbability;
    }
    /**
     * @hidden
     */
    public List<Car> getCarCollection() {
        return carCollection;
    }
    /**
     * @hidden
     */
    public List<Car> getSoldCars() {
        return soldCars;
    }
    /**
     * @hidden
     */
    public void addCustomerAtTheDealership(Customer customer){
        if (customer != null) {
            customerAtTheDealership.add(customer);
        }
    }
    /**
     * @hidden
     */
    public void removeCustomerAtTheDealership(Customer customer){
        if (customer != null) {
            customerAtTheDealership.remove(customer);
        }
    }
}
