package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import eduni.distributions.Negexp;
import simu.framework.*;
import simu.model.servicepoints.ArrivalServicePoint;
import simu.model.servicepoints.ClosureServicePoint;
import simu.model.servicepoints.FinanceServicePoint;
import simu.model.servicepoints.TestdriveServicePoint;

import java.util.ArrayList;
import java.util.Random;

public class MyEngine extends Engine {
	private ArrivalProcess arrivalProcess;
	private ServicePoint[] servicePoints;
	private ArrayList<Customer> processedCustomers = new ArrayList<>();

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
			servicePoints[0] = new ArrivalServicePoint(new Normal(10, 5), eventList, EventType.DEP1);
			servicePoints[1] = new FinanceServicePoint(new Normal(30, 10), eventList, EventType.DEP2);
			servicePoints[2] = new TestdriveServicePoint(new Normal(45, 15), eventList, EventType.DEP3);
			servicePoints[3] = new ClosureServicePoint(new Normal(10, 5), eventList, EventType.DEP4);

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
				Trace.out(Trace.Level.INFO, "queue"+ servicePoints[0].queue);
				customer = servicePoints[0].removeQueue();
				servicePoints[1].addQueue(customer);
				break;

			case DEP2:
				Trace.out(Trace.Level.INFO, "Finance queue"+ servicePoints[1].queue);
				customer = servicePoints[1].peekQueue();
				Trace.out(Trace.Level.INFO, "Finance customer"+ customer);
				if (customer.isFinanceAccepted()) {
					customer = servicePoints[1].removeQueue();
					servicePoints[2].addQueue(customer);
					Trace.out(Trace.Level.INFO, "Testdrivequeue addition"+ servicePoints[2].queue);
					break;
				} else {
					Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " stuck in finance queue.");
				}

			case DEP3:
				Trace.out(Trace.Level.INFO, "Testdrive queue"+ servicePoints[2].queue);
				customer = servicePoints[2].peekQueue();
				Trace.out(Trace.Level.INFO, "Testdrive customer"+ customer + "and Testdrive finance" + customer.isFinanceAccepted());
				if (customer.happyWithTestdrive()) {
					customer = servicePoints[2].removeQueue();
					servicePoints[3].addQueue(customer);
					Trace.out(Trace.Level.INFO, "Closurequeue addition"+ servicePoints[3].queue);
					break;
				} else {
					Trace.out(Trace.Level.WAR, "Customer #" + customer.getId() + " stuck in Testdrive queue.");
				}

			case DEP4:
				Trace.out(Trace.Level.INFO, "Closure queue"+ servicePoints[3]);
				customer = servicePoints[3].removeQueue();
				Trace.out(Trace.Level.INFO, "Closure customer"+ customer);
				if (customer != null) {
					processedCustomers.add(customer);
					customer.setRemovalTime(Clock.getInstance().getClock());
					customer.setTotalTime();
					customer.reportResults();
					break;
				}

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
		System.out.println("Processed Customers: " + processedCustomers.size());
		// Loop through the processedCustomers list
		for (Customer customer : processedCustomers) {
			// Print the header if it's the first customer (you can also print the header just once outside the loop)
			if (processedCustomers.indexOf(customer) == 0) {
				System.out.println(String.format(
						"%-10s %-15s %-15s %-15s %-20s %-15s %-10s %-12s %-18s %-22s %-20s",
						"ID", "Arrival Time", "Removal Time", "Total time", "Preferred Car Type", "Fuel Type",
						"Budget", "Credit Score", "Finance Accepted", "Happy with Test-drive", "Purchased a Car"
				));
			}

			// Print the formatted customer data
			System.out.println(String.format(
					"%-10d %-15.2f %-15.2f %-15.2f %-20s %-15s %-10.2f %-12d %-18b %-22b %-20b",
					customer.getId(),
					customer.getArrivalTime(),
					customer.getRemovalTime(),
					customer.getTotalTime(),
					customer.getPreferredCarType(),
					customer.getPreferredFuelType(),
					customer.getBudget(),
					customer.getCreditScore(),
					customer.isFinanceAccepted(),
					customer.happyWithTestdrive(),
					customer.isPurchased()
			));
		}

	}
}
