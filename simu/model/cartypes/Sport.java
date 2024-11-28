package simu.model.cartypes; 

import simu.model.Car;

public class Sport extends Car {
    public Sport(double baseProb, double meanPrice, double priceVariance, double basePrice, double coefficient) {
        super(baseProb, meanPrice, priceVariance, basePrice, coefficient);
    }
}