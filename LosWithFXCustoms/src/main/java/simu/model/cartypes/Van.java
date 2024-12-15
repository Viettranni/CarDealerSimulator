package simu.model.cartypes; 

import simu.model.Car;

/**
 * Represents a van-type car in the simulation.
 */
public class Van extends Car {

    /**
     * Constructs a van with specified fuel type, mean price, and price variance.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     */
    public Van(String fuelType, double meanPrice, double priceVariance) {
        super("Van", fuelType, meanPrice, priceVariance);
    }

    /**
     * Constructs a van with base price added.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     * @param basePrice     The base price of the van.
     */
    public Van(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("Van", fuelType, meanPrice, priceVariance, basePrice);
    }
}