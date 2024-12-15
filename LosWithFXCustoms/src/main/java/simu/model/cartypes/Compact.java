package simu.model.cartypes; 

import simu.model.Car;

/**
 * Represents a Sedan-type car in the simulation.
 */
public class Compact extends Car {

    /**
     * Constructs a Compact with specified fuel type, mean price, and price variance.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     */
    public Compact(String fuelType, double meanPrice, double priceVariance) {
        super("Compact", fuelType, meanPrice, priceVariance);
    }

    /**
     * Constructs a Compact with base price added.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     * @param basePrice     The base price of the van.
     */
    public Compact(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Compact", fuelType, meanPrice, priceVariance, basePrice);
    }
}