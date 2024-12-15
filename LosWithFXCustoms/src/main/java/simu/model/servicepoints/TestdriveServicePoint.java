package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.*;

/**
 * Represents a test drive service point in the simulation.
 *
 * <p>This class extends the generic <code>ServicePoint</code> and specializes in handling
 * customer test drive requests. It determines car availability, manages customer satisfaction
 * with the test drive, and handles scenarios where customers request multiple test drives.
 * Test drive service points are the third interaction point for customers in the simulation.</p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Processes customer requests for test drives.</li>
 *   <li>Handles scenarios where customers are unhappy with the test drive or their preferred car is unavailable.</li>
 *   <li>Updates customer attributes based on test drive outcomes and schedules the next steps in the simulation.</li>
 * </ul>
 *
 * @see ServicePoint
 */
public class TestdriveServicePoint extends ServicePoint {
    CarDealerShop carDealerShop;

    /**
     * Constructs a test drive service point with the specified attributes.
     *
     * @param generator      A distribution generator used to determine service times.
     * @param eventList      The event list to schedule future simulation events.
     * @param type           The event type associated with this service point.
     * @param carDealerShop  The car dealership associated with this service point, used for managing car availability.
     */
    public TestdriveServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, CarDealerShop carDealerShop) {
        super(generator, eventList, type, "testdrive");
        this.carDealerShop = carDealerShop;
    }

    /**
     * Returns the current customer to the back of the queue.
     *
     * <p>This method removes the customer from the front of the queue and re-adds them to the end of the queue.
     * It is used in scenarios where the customer's preferred car is unavailable or they wish to test drive another car.</p>
     *
     * <p><strong>Note:</strong> If the queue is empty, this method exits without performing any actions.</p>
     */
    public void customerBackToQueue() {
        Customer customer = queue.poll();  // Remove the customer from the front of the queue
        if (customer != null) {
            addQueue(customer);  // Add the customer to the back of the queue
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " moved to the back of the queue.");
        }
    }

    /**
     * Begins service for the customer at the test drive service point.
     *
     * <p>This method processes the customer at the front of the queue. It checks the availability
     * of the customer's preferred car and determines their satisfaction with the test drive. If the car
     * is unavailable or the customer is unsatisfied, they may be returned to the queue for another attempt.</p>
     *
     * <p><strong>Edge Cases:</strong></p>
     * <ul>
     *   <li>If the queue is empty, this method exits without taking action.</li>
     *   <li>If a customer is unhappy after three test drives, they leave without proceeding further.</li>
     * </ul>
     */
    @Override
    public void beginService() {
        if (!isOnQueue()) {
            Trace.out(Trace.Level.INFO, "No customers in queue for test drive.");
            return;  // No customer in the queue
        }

        Customer customer = queue.peek();  // Get the customer at the front of the queue
        if (customer == null) {
            Trace.out(Trace.Level.WAR, "No customer in the queue to serve.");
            return;
        }
        customer.setCurrentServicePoint("testdrive");
        if (customer.getTestDriveCount() <= 1) {
            //customer.setArrivalTimeAtTestDriveServicePoint(Clock.getInstance().getClock());
        }
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is requesting to test drive a " + customer.getPreferredCarType());

        // Check if the preferred car is available
        boolean carAvailable = false;
        Car testdriveCar = null;
        for (Car car : carDealerShop.getCarCollection()) {
            if (car.getCarType().equals(customer.getPreferredCarType()) && car.getFuelType().equals(customer.getPreferredFuelType())) {
                carAvailable = true;
                testdriveCar = car;
                Trace.out(Trace.Level.INFO, "Car available");
                break;
            }
        }
        // boolean carAvailable1 = Math.random() > 0.1;// 90% chance the car is available
        if (!carAvailable) {
            Trace.out(Trace.Level.INFO, "Car not available");
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + "'s preferred car is not available. Returning to queue.");
            customerBackToQueue();  // Put the customer back in the queue
            return;
        }
        if (customer.getTestDriveCount() >= 3){
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " had three testdrives and wasn't happy with " +
                    "the test drives. Customer #" + customer.getId() + " leaves now.");
            customer.setHappyWithTestdrive(false);
            // Call the base method to handle service reservation and event scheduling
            super.beginService();
        }

        // Customer is test-driving
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is test driving.");
        // Determine if the customer likes the car
        boolean likesCar = Math.random() > 0.2;  // 80% chance they like the car
        if (!likesCar) {
            // 65 % chance the customer would like to have another test drive;
            if (Math.random() > 0.65) {
                Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " did not like the car. Returning to queue for another test drive.");
                customerBackToQueue();// Return the customer to the queue
                customer.increaseTestDriveCount();
            } else {
                customer.setHappyWithTestdrive(false);
                // Call the base method to handle service reservation and event scheduling
                super.beginService();
            }
            return;
        } else {
            // Customer liked the car and is ready to proceed
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " liked the car and will proceed.");
            customer.setHappyWithTestdrive(true);  // Mark the customer as happy with the test drive
            customer.setPurchaseCar(testdriveCar);
            // Call the base method to handle service reservation and event scheduling
            super.beginService();
        }
    }
}
