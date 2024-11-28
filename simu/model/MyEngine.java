package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import eduni.distributions.Negexp;
import simu.framework.*;
import simu.model.servicepoints.ArrivalServicePoint;
import simu.model.servicepoints.ClosureServicePoint;
import simu.model.servicepoints.FinanceServicePoint;
import simu.model.servicepoints.TestdriveServicePoint;

import java.util.Random;

public class MyEngine extends Engine {
	private ArrivalProcess arrivalProcess;
	private ServicePoint[] servicePoints;

	public static final boolean TEXTDEMO = true;
	public static final boolean FIXEDARRIVALTIMES = false;
	public static final boolean FIXEDSERVICETIMES = false;

	/*
	 * This is the place where you implement your own simulator
	 *
	 * Demo simulation case:
	 * Simulate four service points:
	 * ArrivalServicePoint -> FinanceServicePoint -> TestdriveServicePoint -> ClosureServicePoint
	 */
	public MyEngine() {
		servicePoints = new ServicePoint[4]; // Updated for four service points

		if (TEXTDEMO) {
			// Special setup for text example
			Random r = new Random();

			ContinuousGenerator arrivalTime = null;
			if (FIXEDARRIVALTIMES) {
				// Fixed customer arrival times
				arrivalTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 10;
					}

					@Override
					public void setSeed(long seed) {}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {}
				};
			} else {
				// Exponential distribution for variable customer arrival times
				arrivalTime = new Negexp(10, Integer.toUnsignedLong(r.nextInt()));
			}

			ContinuousGenerator serviceTime = null;
			if (FIXEDSERVICETIMES) {
				// Fixed service times
				serviceTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 9;
					}

					@Override
					public void setSeed(long seed) {}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {}
				};
			} else {
				// Normal distribution for variable service times
				serviceTime = new Normal(10, 6, Integer.toUnsignedLong(r.nextInt()));
			}

			// Initialize specialized service points
			servicePoints[0] = new ArrivalServicePoint(serviceTime, eventList, EventType.DEP1);
			servicePoints[1] = new FinanceServicePoint(serviceTime, eventList, EventType.DEP2);
			servicePoints[2] = new TestdriveServicePoint(serviceTime, eventList, EventType.DEP3);
			servicePoints[3] = new ClosureServicePoint(serviceTime, eventList, EventType.DEP4);

			arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);
		} else {
			// Realistic simulation with variable arrival and service times
			servicePoints[0] = new ArrivalServicePoint(new Normal(10, 6), eventList, EventType.DEP1);
			servicePoints[1] = new FinanceServicePoint(new Normal(10, 10), eventList, EventType.DEP2);
			servicePoints[2] = new TestdriveServicePoint(new Normal(5, 3), eventList, EventType.DEP3);
			servicePoints[3] = new ClosureServicePoint(new Normal(7, 4), eventList, EventType.DEP4);

			arrivalProcess = new ArrivalProcess(new Negexp(15, new Random().nextLong()), eventList, EventType.ARR1);
		}
	}

	@Override
	protected void initialize() { // First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	@Override
	protected void runEvent(Event t) { // B phase events
		Customer customer;

		switch ((EventType) t.getType()) {
			case ARR1:
				servicePoints[0].addQueue(new Customer());
				arrivalProcess.generateNextEvent();
				break;

			case DEP1:
				customer = servicePoints[0].removeQueue();
				servicePoints[1].addQueue(customer);
				break;

			case DEP2:
				customer = servicePoints[1].removeQueue();
				servicePoints[2].addQueue(customer);
				break;

			case DEP3:
				customer = servicePoints[2].removeQueue();
				servicePoints[3].addQueue(customer);
				break;

			case DEP4:
				customer = servicePoints[3].removeQueue();
				customer.setRemovalTime(Clock.getInstance().getClock());
				customer.reportResults();
				break;
		}
	}

	@Override
	protected void tryCEvents() {
		for (ServicePoint point : servicePoints) {
			if (!point.isReserved() && point.isOnQueue()) {
				point.beginService();
			}
		}
	}

	@Override
	protected void results() {
		System.out.println("Simulation ended at " + Clock.getInstance().getClock());
		System.out.println("Results are currently missing");
	}
}
