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
        Trace.out(Trace.Level.INFO, "Checking finances for Customer #" + customer.getId());

        if (customer.getCreditScore() < 600) {  // Example credit score threshold
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " rejected due to poor credit score.");
            removeQueue();  // Remove customer
        } else {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " approved for financing.");
            super.beginService();  // Proceed to schedule the next event
        }
    }
}
