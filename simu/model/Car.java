package simu.model;
import eduni.distributions.Normal;

import java.util.ArrayList;


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
    private double carTypeMean;
    private double carTypeVariance = 10000;
    private double saleProb;
    private volatile double coefficient;

    public Car(String carType, String fuelType, double meanPrice, double priceVariance) {
        this.carType = carType;
        this.carTypeProb = setCarTypeProb(carType);
        this.fuelType = fuelType;
        this.fuelTypeProb = setFuelTypeProb(fuelType);
        this.baseProb = carTypeProb * fuelTypeProb;
        this.meanPrice = meanPrice;
        this.priceVariance = priceVariance;
        this.carTypeMean = setCarTypeMean(carType);
        this.basePrice = new Normal(carTypeMean, carTypeVariance).sample();
        this.sellerPrice = new Normal(meanPrice, priceVariance).sample();
        this.coefficient = setCoefficient(carType);

        // Generating the unique register number for every Vehicle object
        this.registerNumber = CarDealerShop.generateUniqueRegisterNumber();
    }

    // Getters and Setters
    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getCarType() {
        return carType;
    }
    public void setCarType(String carType) {
        this.carType = carType;
    }
    public double getCarTypeProb() {
        return carTypeProb;
    }

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

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getFuelTypeProb() {
        return fuelTypeProb;
    }

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


    public double getBaseProb() {
        return baseProb;
    }

    public void setBaseProb(double baseProb) {
        this.baseProb = baseProb;
    }

    public double getMeanPrice() {
        return meanPrice;
    }

    public void setMeanPrice(double meanPrice) {
        this.meanPrice = meanPrice;
    }

    public double getSellerPrice(){
        return sellerPrice;
    }

    public void setSellerPrice(double sellerPrice) {
        this.sellerPrice = sellerPrice;
    }

    public double getPriceVariance() {
        return priceVariance;
    }

    public void setPriceVariance(double priceVariance) {
        this.priceVariance = priceVariance;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double setCarTypeMean(String carType) {
        switch (carType.toLowerCase()) {
            case "compact":
                carTypeMean = 20000;
                break;
            case "van":
                carTypeMean = 30000;
                break;
            case "suv":
                carTypeMean = 40000;
                break;
            case "sedan":
                carTypeMean = 30000;
                break;
            case "sport":
                carTypeMean = 60000;
                break;
            default:
                carTypeMean = 15000;
                break;
        }
        return carTypeMean;
    }

    public double getCarTypeMean() {
        return carTypeMean;
    }

    public double setCarTypeVariance(String carType) {
        switch (carType.toLowerCase()) {
            case "compact":
                carTypeVariance = 3000; // Compact cars have low variability in price
                break;
            case "van":
                carTypeVariance = 4000; // Vans have moderate variability
                break;
            case "suv":
                carTypeVariance = 5000; // SUVs have moderate-to-high variability
                break;
            case "sedan":
                carTypeVariance = 4000; // Sedans are moderately variable
                break;
            case "sport":
                carTypeVariance = 10000; // Sports cars exhibit high price variability
                break;
            default:
                carTypeVariance = 2000; // Default variance for unknown car types
                break;
        }
        return carTypeVariance;
    }


    public double getCarTypeVariance() {
        return carTypeVariance;
    }



    public double getSaleProb() {
        return saleProb;
    }

    public void setSaleProb(double saleProb) {
        this.saleProb = saleProb;
    }

    public synchronized double getCoefficient() {
        return coefficient;
    }

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

    // Methods
    public double calculateCarTypeProb() {
        return coefficient * baseProb;
    }

    public static void createCar(String carType, int sellerCarMean, int sellerCarVariance, ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel) {
        String amountOfCars = String.valueOf(numberOfCars);
        String fuelType = String.valueOf(typeOfFuel);
        String carMean = String.valueOf(sellerCarMean);
        String carVariance = String.valueOf(sellerCarVariance);

        // Add each car to the list
        carsToBeCreated.add(new String[]{carType, amountOfCars, fuelType, carMean, carVariance});

    }

    // Method to calculate sale probability
    public double calculateSaleProbability(double sellerPrice) {
        // Sales probability formula:
        // saleProbability = basePrice * e^(-coefficient * (customerPrice - basePrice))
        saleProb = baseProb * Math.exp(-coefficient * (sellerPrice - basePrice));
        return saleProb;
    }
}