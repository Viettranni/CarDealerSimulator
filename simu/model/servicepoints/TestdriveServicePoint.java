package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;

import java.util.Arrays;

public class TestdriveServicePoint extends ServicePoint {
    public TestdriveServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type);
    }

    public void customerBackToQueue() {
        Customer c = queue.poll();
        addQueue(c);
    }

    @Override
    public void beginService() {
        if (!isOnQueue()) return;

        Customer customer = queue.peek();
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is requesting to test drive a " + customer.getPreferredCarType());

        boolean carAvailable = Math.random() > 0.1;  // 90% chance the car is available
        if (!carAvailable) {
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + "'s preferred car is not available. Returning to queue.");
            customerBackToQueue();
        } else {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is test driving.");
            super.beginService();  // Simulate test drive

            boolean likesCar = Math.random() > 0.8;  // 80% chance they like the car
            if (!likesCar) {
                Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " did not like the car. Returning to queue for another test drive.");
                customerBackToQueue();
                /* Currently myEngine removes the first customer in the queue.
                   In this case if the current customer wants a new test drive
                   The customer will be added to the back of the queue BUT
                   the customer from the front will be removed WITHOUT test-driving
                   Fix is in the work.
                   */

            } else {
                Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " liked the car and will proceed.");
            }
            Trace.out(Trace.Level.INFO, "queue"+ queue.toString());
        }
    }
}
