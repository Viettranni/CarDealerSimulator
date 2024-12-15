package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.MyEngine;
import simu.model.ServicePoint;

/**
 * Represents a finance service point in the simulation.
 *
 * <p>This class extends the generic <code>ServicePoint</code> and specializes in handling
 * the financial approval process for customers. It evaluates each customer's credit score
 * and determines whether they qualify for financing. Customers who are approved proceed
 * to the next stage in the simulation. Finance service points are the second interaction
 * point for customers in the simulation.</p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Processes customer financing requests and determines approval.</li>
 *   <li>Logs financing outcomes for each customer.</li>
 *   <li>Delegates service start logic to the parent <code>ServicePoint</code>.</li>
 * </ul>
 *
 * @see ServicePoint
 */
public class FinanceServicePoint extends ServicePoint {
    /**
     * Constructs a finance service point with the specified attributes.
     *
     * @param generator  A distribution generator used to determine service times.
     * @param eventList  The event list to schedule future simulation events.
     * @param type       The event type associated with this service point.
     */
    public FinanceServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type, "finance");
    }

    /**
     * Begins service for the customer at the finance service point.
     *
     * <p><strong>Key Steps:</strong></p>
     * <ul>
     *   <li>Checks the queue and retrieves the first customer.</li>
     *   <li>Logs the customer's interaction at the finance service point.</li>
     *   <li>Calculates the probability of financing approval based on the customer's credit score.</li>
     *   <li>Logs whether the customer was approved or rejected for financing.</li>
     *   <li>Delegates further service logic to the parent <code>ServicePoint</code>.</li>
     * </ul>
     *
     *<p><strong>Note:</strong> If the queue is empty, this method exits without taking action.</p>
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
        customer.setCurrentServicePoint("finance");
        //customer.setArrivalTimeAtFinanceServicePoint(Clock.getInstance().getClock());
        Trace.out(Trace.Level.INFO, "Checking finances for Customer #" + customer.getId());
        double customerCreditScore = customer.getCreditScore();
        boolean financeAccepted = Math.random() < customer.calculateFinanceProbability(customerCreditScore);
        if (!financeAccepted) {  // Example credit score threshold
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " rejected due to poor credit score.");
            super.beginService();// Proceed to schedule the next event
        } else {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " approved for financing.");
            customer.setFinanceAccepted(true);
            super.beginService();// Proceed to schedule the next event
            Trace.out(Trace.Level.INFO, "queue"+ queue.toString());
        }
    }
}
