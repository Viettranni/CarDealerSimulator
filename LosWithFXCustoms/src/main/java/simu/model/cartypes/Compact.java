package simu.model.cartypes; 

import simu.model.Car;

public class Compact extends Car {
    public Compact(String fuelType, double meanPrice, double priceVariance) {
        super("Compact", fuelType, meanPrice, priceVariance);
    }

    public Compact(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Compact", fuelType, meanPrice, priceVariance, basePrice);
    }
}