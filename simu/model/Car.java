package simu.model;
import eduni.distributions.Normal;


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
        this.basePrice = new Normal(meanPrice, priceVariance).sample();
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
            case "compact", "sport":
                carTypeProb = 0.1;
                break;
            case "van":
                carTypeProb = 0.15;
                break;
            case "suv":
                carTypeProb = 0.30;
                break;
            case "sedan":
                carTypeProb = 0.35;
                break;
            default:
                carTypeProb = 0.20;
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
                fuelTypeProb = 0.10;
                break;
            case "hybrid":
                fuelTypeProb = 0.30;
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
                coefficient = 0.04;
                break;
            case "van":
                coefficient = 0.06;
                break;
            case "sedan":
                coefficient = 0.08;
                break;
            case "compact":
                coefficient = 0.10;
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

    // Method to calculate sale probability
    public double calculateSaleProbability(double customerPrice) {
        // Sales probability formula:
        // saleProbability = basePrice * e^(-coefficient * (customerPrice - basePrice))
        saleProb = basePrice * Math.exp(-coefficient * (basePrice - customerPrice));
        return saleProb;
    }
}