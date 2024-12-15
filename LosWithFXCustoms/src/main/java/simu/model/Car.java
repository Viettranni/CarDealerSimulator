package simu.model;
import eduni.distributions.Normal;

import java.util.ArrayList;
import java.util.HashMap;


public class Car {
    private String registerNumber;
    private String carType;
    private double carTypeProb;
    private String fuelType;
    private double fuelTypeProb;
    private double baseProb;
    private double meanPrice; 
    private double priceVariance;
    private double basePrice;
    private double sellerPrice;
    private double saleProb;
    private volatile double coefficient;
    private static final HashMap<String, Double> basePrices = new HashMap<>() {{
        put("suv", 40000.0);
        put("sedan", 30000.0);
        put("van", 30000.0);
        put("compact", 20000.0);
        put("sport", 60000.0);
    }};

    /**
     * Constructor for creating a car object with specified type, fuel type, mean price, and price variance.
     * It initializes car-specific probabilities, base price, and a unique register number.
     *
     * @param carType       The type of the car (e.g., SUV, Sedan).
     * @param fuelType      The fuel type of the car (e.g., Electric, Gasoline).
     * @param meanPrice     The average price of the car.
     * @param priceVariance The variance in the car's price.
     */
    public Car(String carType, String fuelType, double meanPrice, double priceVariance) {
        this.carType = carType;
        this.carTypeProb = setCarTypeProb(carType);
        this.fuelType = fuelType;
        this.fuelTypeProb = setFuelTypeProb(fuelType);
        this.baseProb = carTypeProb * fuelTypeProb;
        this.meanPrice = meanPrice;
        this.priceVariance = priceVariance;
        this.basePrice = setBasePrice(carType);
        this.sellerPrice = new Normal(meanPrice, priceVariance).sample();
        this.coefficient = setCoefficient(carType);

        // Generating the unique register number for every Vehicle object
        this.registerNumber = CarDealerShop.generateUniqueRegisterNumber();
    }

    /**
     * Constructor for creating a car object with base price specified.
     * It initializes car-specific probabilities, seller price, and a unique register number.
     *
     * @param carType       The type of the car (e.g., SUV, Sedan).
     * @param fuelType      The fuel type of the car (e.g., Electric, Gasoline).
     * @param meanPrice     The average price of the car.
     * @param priceVariance The variance in the car's price.
     * @param basePrice     The base price of the car.
     */
    public Car(String carType, String fuelType, double meanPrice, double priceVariance, double basePrice) {
        this.carType = carType;
        this.carTypeProb = setCarTypeProb(carType);
        this.fuelType = fuelType;
        this.fuelTypeProb = setFuelTypeProb(fuelType);
        this.baseProb = carTypeProb * fuelTypeProb;
        this.meanPrice = meanPrice;
        this.priceVariance = priceVariance;
        this.basePrice = basePrice;
        this.sellerPrice = new Normal(meanPrice, priceVariance).sample();
        this.coefficient = setCoefficient(carType);

        // Generating the unique register number for every Vehicle object
        this.registerNumber = CarDealerShop.generateUniqueRegisterNumber();
    }

    /**
     * @hidden
     */
    public double calculateCarTypeProb() {
        return coefficient * baseProb;
    }

    /**
     * Calculates the sale probability of the car based on its seller price.
     * The formula uses an exponential decay function with the coefficient and base price.
     *
     * @param sellerPrice The seller's price of the car.
     * @return The calculated sale probability.
     */
    public double calculateSaleProbability(double sellerPrice) {
        // Sales probability formula:
        // saleProbability = basePrice * e^(-coefficient * (sellerPrice - basePrice))
        saleProb = baseProb * Math.exp(-coefficient * (sellerPrice - basePrice));
        return saleProb;
    }


    // Getters and Setters
    /**
     * @hidden
     */
    public String getRegisterNumber() {
        return registerNumber;
    }
    /**
     * @hidden
     */
    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }
    /**
     * @hidden
     */
    public String getCarType() {
        return carType;
    }
    /**
     * @hidden
     */
    public void setCarType(String carType) {
        this.carType = carType;
    }
    /**
     * @hidden
     */
    public double getCarTypeProb() {
        return carTypeProb;
    }
    /**
     * @hidden
     */
    public double setCarTypeProb(String carType) {
        switch (carType.toLowerCase()) {
            case "sport":
                carTypeProb = 0.15;
                break;
            case "van":
                carTypeProb = 0.20;
                break;
            case "compact":
                carTypeProb = 0.25;
                break;
            case "suv":
                carTypeProb = 0.30;
                break;
            case "sedan":
                carTypeProb = 0.35;
                break;
            default:
                carTypeProb = 0.24;
                break;
        }
        return carTypeProb;
    }
    /**
     * @hidden
     */
    public String getFuelType() {
        return fuelType;
    }
    /**
     * @hidden
     */
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    /**
     * @hidden
     */
    public double getFuelTypeProb() {
        return fuelTypeProb;
    }
    /**
     * @hidden
     */
    public double setFuelTypeProb(String fuelType) {
        switch (fuelType.toLowerCase()) {
            case "electric":
                fuelTypeProb = 0.20;
                break;
            case "hybrid":
                fuelTypeProb = 0.40;
                break;
            case "gasoline":
                fuelTypeProb = 0.60;
                break;
            default:
                fuelTypeProb = 0.50; // Default probability
                break;
        }
        return fuelTypeProb;
    }
    /**
     * @hidden
     */
    public double getBaseProb() {
        return baseProb;
    }
    /**
     * @hidden
     */
    public void setBaseProb(double baseProb) {
        this.baseProb = baseProb;
    }
    /**
     * @hidden
     */
    public double getMeanPrice() {
        return meanPrice;
    }
    /**
     * @hidden
     */
    public void setMeanPrice(double meanPrice) {
        this.meanPrice = meanPrice;
    }
    /**
     * @hidden
     */
    public double getSellerPrice(){
        return sellerPrice;
    }
    /**
     * @hidden
     */
    public void setSellerPrice(double sellerPrice) {
        this.sellerPrice = sellerPrice;
    }
    /**
     * @hidden
     */
    public double getPriceVariance() {
        return priceVariance;
    }
    /**
     * @hidden
     */
    public void setPriceVariance(double priceVariance) {
        this.priceVariance = priceVariance;
    }
    /**
     * @hidden
     */
    public double getBasePrice() {
        return basePrice;
    }
    /**
     * @hidden
     */
    public static HashMap<String, Double> getBasePrices(){
        return basePrices;
    }
    /**
     * @hidden
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    /**
     * @hidden
     */
    public double setBasePrice(String carType) {
        switch (carType.toLowerCase()) {
            case "compact":
                basePrice = 20000;
                break;
            case "van":
                basePrice = 30000;
                break;
            case "suv":
                basePrice = 40000;
                break;
            case "sedan":
                basePrice = 30000;
                break;
            case "sport":
                basePrice = 60000;
                break;
            default:
                basePrice = 15000;
                break;
        }
        return basePrice;
    }
    /**
     * @hidden
     */
    public double getSaleProb() {
        return saleProb;
    }
    /**
     * @hidden
     */
    public void setSaleProb(double saleProb) {
        this.saleProb = saleProb;
    }
    /**
     * @hidden
     */
    public synchronized double getCoefficient() {
        return coefficient;
    }
    /**
     * @hidden
     */
    public synchronized double setCoefficient(String carType) {
        switch (carType.toLowerCase()) {
            case "sport":
                coefficient = 0.02;
                break;
            case "suv":
                coefficient = 0.03;
                break;
            case "van":
                coefficient = 0.06;
                break;
            case "sedan":
                coefficient = 0.05;
                break;
            case "compact":
                coefficient = 0.07;
                break;
            default:
                coefficient = 0.05;
                break;
        }
        return coefficient;
    }

}