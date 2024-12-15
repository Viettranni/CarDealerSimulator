package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;

/**
 * Represents an arrival service point in the simulation.
 *
 * <p>This class extends the generic <code>ServicePoint</code> and specializes in handling
 * customer arrivals. It manages the queue of arriving customers, initializes their
 * attributes, and starts their service process. Arrival service points are the first
 * interaction point for customers in the simulation.</p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Handles customer arrival events and manages the arrival queue.</li>
 *   <li>Logs customer information and updates simulation metrics.</li>
 *   <li>Delegates service start logic to the parent <code>ServicePoint</code>.</li>
 * </ul>
 *
 * @see ServicePoint
 */
public class ArrivalServicePoint extends ServicePoint {

    /**
     * Constructs an arrival service point with the specified attributes.
     *
     * @param generator  A distribution generator used to determine service times.
     * @param eventList  The event list to schedule future simulation events.
     * @param type       The event type associated with this service point.
     */
    public ArrivalServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type, "arrival");
    }


    /**
     * Begins service for the first customer of the queue.
     *
     * <p><strong>Key Actions:</strong></p>
     * <ul>
     *   <li>Checks the queue and retrieves the first customer.</li>
     *   <li>Updates the customer's current service point attribute.</li>
     *   <li>Logs the customer's arrival at the service point.</li>
     *   <li>Delegates to the <code>ServicePoint.beginService</code> for additional processing.</li>
     * </ul>
     *
     * <p><strong>Note:</strong> If no customers are in the queue, this method method exits.</p>
     */
    @Override
    public void beginService() {
        if (!isOnQueue()) return;

        Customer customer = queue.peek();  // Get the customer at the front of the queue
        if (customer == null) {
            Trace.out(Trace.Level.WAR, "No customer in the queue to serve.");
            return;
        }
        customer.setCurrentServicePoint("arrival");
        //customer.setArrivalTimeAtArrivalServicePoint(Clock.getInstance().getClock());
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is welcomed at the arrival point.");
        super.beginService();
    }
}
