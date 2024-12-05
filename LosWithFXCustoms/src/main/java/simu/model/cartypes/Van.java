package simu.model.cartypes; 

import simu.model.Car;

public class Van extends Car {
    public Van(String fuelType, double meanPrice, double priceVariance) {
        super("Van", fuelType, meanPrice, priceVariance);
    }

    public Van(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Van", fuelType, meanPrice, priceVariance, basePrice);
    }
}