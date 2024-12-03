package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;

public class ArrivalServicePoint extends ServicePoint {
    public ArrivalServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type);
    }

    @Override
    public void beginService() {
        if (!isOnQueue()) return;

        Customer customer = queue.peek();
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is welcomed at the arrival point.");
        super.beginService();
    }
}
