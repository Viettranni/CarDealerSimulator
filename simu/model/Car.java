package simu.model;


public class Car {
    private String registerNumber;
    private String fuelType;
    private double baseProb;
    private double meanPrice; 
    private double priceVariance;
    private double basePrice;
    private double saleProb;
    private double coefficient;

    public Car(double baseProb, double meanPrice, double priceVariance, double basePrice, double coefficient) {
        this.baseProb = baseProb;
        this.meanPrice = meanPrice;
        this.priceVariance = priceVariance;
        this.basePrice = basePrice;
        this.coefficient = coefficient;

        // Generating the unique registernumber for every Vehicle object
        this.registerNumber = CarDealerShop.generateUniqueRegisterNumber();
    }

    // Getters and Setters
    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
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

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    // Methods ----------------------------------------------------------------------------------------

    // Calculating Car Type Probability
    public double calculateCarTypeProb() {
        return coefficient * baseProb;
    }

    // Break even price for Vehicle
    public double getBreakEvenPrice() {
        return basePrice;
    }
    
    // Calculating the inventory value
    public double getInventoryValue(int quantity) {
        return quantity * basePrice;
    }

    
}