package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;


public class FinanceServicePoint extends ServicePoint {
    public FinanceServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
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
        Trace.out(Trace.Level.INFO, "Checking finances for Customer #" + customer.getId());

        if (customer.getCreditScore() < 600) {  // Example credit score threshold
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " rejected due to poor credit score.");
            queue.poll();  // Remove customer
        } else {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " approved for financing.");
            customer.setFinanceAccepted(true);
            super.beginService();// Proceed to schedule the next event
            Trace.out(Trace.Level.INFO, "queue"+ queue.toString());
        }
    }
}
