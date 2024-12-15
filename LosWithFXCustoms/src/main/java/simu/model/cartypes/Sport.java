package simu.model.cartypes; 

import simu.model.Car;

/**
 * Represents a Sport-type car in the simulation.
 */
public class Sport extends Car {
    /**
     * Constructs a Sports car with specified fuel type, mean price, and price variance.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     */
    public Sport(String fuelType, double meanPrice, double priceVariance) {
        super("Sport", fuelType, meanPrice, priceVariance);
    }

    /**
     * Constructs a Sports car with base price added.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     * @param basePrice     The base price of the van.
     */
    public Sport(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Sport", fuelType, meanPrice, priceVariance, basePrice);
    }
}