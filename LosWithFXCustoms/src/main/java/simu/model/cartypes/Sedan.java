package simu.model.cartypes; 

import simu.model.Car;


public class Sedan extends Car {
    public Sedan(String fuelType, double meanPrice, double priceVariance) {
        super("Sedan", fuelType, meanPrice, priceVariance);
    }

    public Sedan(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Van", fuelType, meanPrice, priceVariance, basePrice);
    }
}