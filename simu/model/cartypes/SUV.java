package simu.model.cartypes; 

import simu.model.Car;


public class SUV extends Car {
    public SUV(double baseProb, double meanPrice, double priceVariance, double basePrice, double coefficient) {
        super(baseProb, meanPrice, priceVariance, basePrice, coefficient);
    }
}