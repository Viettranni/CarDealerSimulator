package simu.model.cartypes; 

import simu.model.Car;

/**
 * Represents a Sedan-type car in the simulation.
 */
public class Sedan extends Car {
    /**
     * Constructs a Sedan with specified fuel type, mean price, and price variance.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     */
    public Sedan(String fuelType, double meanPrice, double priceVariance) {
        super("Sedan", fuelType, meanPrice, priceVariance);
    }

    /**
     * Constructs a Sedan with base price added.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     * @param basePrice     The base price of the van.
     */
    public Sedan(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Sedan", fuelType, meanPrice, priceVariance, basePrice);
    }
}