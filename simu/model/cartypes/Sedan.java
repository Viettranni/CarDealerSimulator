package simu.model.cartypes; 

import simu.model.Car;


public class Sedan extends Car {
    public Sedan(double baseProb, double meanPrice, double priceVariance, double basePrice, double coefficient) {
        super(baseProb, meanPrice, priceVariance, basePrice, coefficient);
    }
}