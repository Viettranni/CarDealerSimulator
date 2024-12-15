package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.*;

/**
 * Represents a closure service point in the simulation.
 *
 * <p>This class extends the generic <code>ServicePoint</code> and specializes in handling
 * the closure phase of a customer's journey. At the closure service point, customers
 * randomly decide will they purchase the car. Closure service points are the very last
 * interaction point for customers in the simulation.</p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Processes customers at the closure phase of the dealership interaction.</li>
 *   <li>Handles purchase decisions based on customer preferences and probabilities.</li>
 *   <li>Updates the car dealership inventory when a purchase is completed.</li>
 * </ul>
 *
 * @see ServicePoint
 */
public class ClosureServicePoint extends ServicePoint {
    CarDealerShop carDealerShop;

    /**
     * Constructs a closure service point with the specified attributes.
     *
     * @param generator      A distribution generator used to determine service times.
     * @param eventList      The event list to schedule future simulation events.
     * @param type           The event type associated with this service point.
     * @param carDealerShop  The car dealership associated with this service point, used for inventory updates.
     */
    public ClosureServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, CarDealerShop carDealerShop) {
        super(generator, eventList, type, "closure");
        this.carDealerShop = carDealerShop;
    }


    /**
     * Begins service for the customer at the closure service point.
     *
     * <p><strong>Key Steps:</strong></p>
     * <ul>
     *   <li>Checks the queue and retrieves the first customer.</li>
     *   <li>Logs the customer's interaction at the closure service point.</li>
     *   <li>Calculates the probability of a successful purchase.</li>
     *   <li>Updates the car dealership inventory if the purchase is successful.</li>
     *   <li>Delegates further service logic to the parent <code>ServicePoint</code>.</li>
     * </ul>
     *
     * <p><strong>Note:</strong> If the queue is empty, this method exits without taking action.</p>
     */
    @Override
    public void beginService() {
        if (!isOnQueue()) return;

        Customer customer = queue.peek();
        if (customer == null) {
            // Handle the case where there's no customer in the queue (shouldn't happen if the check is correct)
            Trace.out(Trace.Level.WAR, "No customer in the queue to serve.");
            return;
        }
        customer.setCurrentServicePoint("closure");
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is closing the deal.");

        Car car = customer.getPurchaseCar();
        double carPrice = car.getSellerPrice();
        boolean dealClosed = Math.random() <= car.calculateSaleProbability(carPrice);
        if (dealClosed) {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " completed the purchase and is leaving.");
            customer.setPurchased(true);
            carDealerShop.sellCar(car.getRegisterNumber());
        } else {
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " decided not to purchase and is leaving.");
        }

        super.beginService();
        Trace.out(Trace.Level.INFO, "queue"+ queue.toString());
    }
}
