package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.EventType;
import simu.model.ServicePoint;
import simu.model.Customer;

public class ClosureServicePoint extends ServicePoint {
    public ClosureServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type);
    }

    @Override
    public void beginService() {
        if (!isOnQueue()) return;

        Customer customer = queue.peek();
        if (customer == null) {
            // Handle the case where there's no customer in the queue (shouldn't happen if the check is correct)
            Trace.out(Trace.Level.WAR, "No customer in the queue to serve.");
            return;
        }

        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is closing the deal.");

        boolean dealClosed = Math.random() > 0.2;  // 80% chance the deal is closed
        if (dealClosed) {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " completed the purchase and is leaving.");
        } else {
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " decided not to purchase and is leaving.");
        }

        super.endService();
    }
}
