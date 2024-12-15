package simu.model.cartypes; 

import simu.model.Car;

/**
 * Represents a SUV-type car in the simulation.
 */
public class SUV extends Car {
    /**
     * Constructs a SUV with specified fuel type, mean price, and price variance.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     */
    public SUV(String fuelType, double meanPrice, double priceVariance) {
        super("SUV", fuelType, meanPrice, priceVariance);
    }

    /**
     * Constructs a SUV with base price added.
     *
     * @param fuelType      The type of fuel the van uses.
     * @param meanPrice     The mean price of the van.
     * @param priceVariance The price variance of the van.
     * @param basePrice     The base price of the van.
     */
    public SUV(String fuelType, double meanPrice, double priceVariance, double basePrice) {
        super("SUV", fuelType, meanPrice, priceVariance, basePrice);
    }
}