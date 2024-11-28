package simu.model.cartypes; 

import simu.model.Car;

public class Van extends Car {
    public Van(double baseProb, double meanPrice, double priceVariance, double basePrice, double coefficient) {
        super(baseProb, meanPrice, priceVariance, basePrice, coefficient);
    }
}