package simu.model.cartypes; 

import simu.model.Car;

public class Compact extends Car {
    public Compact(double baseProb, double meanPrice, double priceVariance, double basePrice, double coefficient) {
        super(baseProb, meanPrice, priceVariance, basePrice, coefficient);
    }
}