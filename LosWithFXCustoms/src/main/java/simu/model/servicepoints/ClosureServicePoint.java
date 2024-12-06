package simu.model.servicepoints;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.*;

public class ClosureServicePoint extends ServicePoint {
    CarDealerShop carDealerShop;
    public ClosureServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, CarDealerShop carDealerShop) {
        super(generator, eventList, type, "closure");
        this.carDealerShop = carDealerShop;
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
        customer.setCurrentServicePoint("closure");
        Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " is closing the deal.");

        Car car = customer.getPurchaseCar();
        double customerPrice = customer.getBudget();
        double carPrice = car.getSellerPrice();
        boolean dealClosed = Math.random() <= car.calculateSaleProbability(carPrice);
        boolean test = true;
        if (dealClosed) {
            Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " completed the purchase and is leaving.");
            customer.setPurchased(true);
            carDealerShop.sellCar(car.getRegisterNumber());
        } else {
            Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " decided not to purchase and is leaving.");
        }

        super.endService();
        Trace.out(Trace.Level.INFO, "queue"+ queue.toString());
    }
}
