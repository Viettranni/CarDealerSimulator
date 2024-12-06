package simu.model.cartypes; 

import simu.model.Car;


public class SUV extends Car {
    public SUV(String fuelType, double meanPrice, double priceVariance) {
        super("SUV", fuelType, meanPrice, priceVariance);
    }

    public SUV(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("SUV", fuelType, meanPrice, priceVariance, basePrice);
    }
}