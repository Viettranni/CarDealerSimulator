package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.*;

public class TestdriveServicePoint extends ServicePoint {
    CarDealerShop carDealerShop;

    public TestdriveServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, CarDealerShop carDealerShop) {
        super(generator, eventList, type, "testdrive");
        this.carDealerShop = carDealerShop;
    }

    /**
     * Handles the case where a customer is returned to the back of the queue.
     */
    public void customerBackToQueue() {
        Customer customer = queue.poll();  // Remove the customer from the front of the queue
        if (customer != null) {
            addQueue(customer);  // Add the customer to the back of the queue
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " moved to the back of the queue.");
        }
    }

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

        // Customer is test-driving
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is test driving.");
        // Determine if the customer likes the car
        boolean likesCar = Math.random() > 0.2;  // 80% chance they like the car
        if (!likesCar) {
            if (Math.random() > 0.65) {
                Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " did not like the car. Returning to queue for another test drive.");
                customerBackToQueue();// Return the customer to the queue
                customer.increaseTestDriveCount();
            } else {
                if (customer != null) {
                    customer.setRemovalTime(Clock.getInstance().getClock());
                    customer.setTotalTime();
                    MyEngine.addProcessedCustomer(customer); // Add to the processed list
                }
            }
            return;
        }
        if (customer.getTestDriveCount() >= 3){
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " had three testdrives and wasn't happy with " +
                    "the test drives. Customer #" + customer.getId() + " leaves now.");
            queue.poll();
            MyEngine.addProcessedCustomer(customer);
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
