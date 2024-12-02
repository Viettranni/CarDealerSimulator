package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import eduni.distributions.Negexp;
import simu.framework.*;
import simu.model.servicepoints.ArrivalServicePoint;
import simu.model.servicepoints.ClosureServicePoint;
import simu.model.servicepoints.FinanceServicePoint;
import simu.model.servicepoints.TestdriveServicePoint;

import java.util.*;

public class MyEngine extends Engine {
	private ArrivalProcess arrivalProcess;
	private ServicePoint[] servicePoints;
	private static ArrayList<Customer> processedCustomers = new ArrayList<>();
	private CarDealerShop carDealerShop;
	private ArrayList<String[]> carsToBeCreated;
	private int arrivalMean;
	private int arrivalVariance;
	private int financeMean;
	private int financeVariance;
	private int testdriveMean;
	private int testdriveVariance;
	private int closureMean;
	private int closureVariance;
	private volatile int simulationSpeed;
	private int arrivalServicePoints;
	private int financeServicePoints;
	private int testdriveServicePoints;
	private int closureServicePoints;
	private int totalServicePoints;
	private int currentServicePointIndex = 0;// Tracks the next free index
	private final int seed = 123;
	ContinuousGenerator serviceTime = null;
	ContinuousGenerator arrivalServiceTime = null;
	ContinuousGenerator financeServiceTime = null;
	ContinuousGenerator testdriveServiceTime = null;
	ContinuousGenerator closureServiceTime = null;
	Random r = null;


	public static final boolean TEXTDEMO = true;
	public static final boolean FIXEDARRIVALTIMES = false;
	public static final boolean FIXEDSERVICETIMES = false;

	/*
	 * This is the place where you implement your own simulator
	 *
	 * Demo simulation case:
	 * Simulate four service points:
	 * ArrivalServicePoint -> FinanceServicePoint -> Test-driveServicePoint -> ClosureServicePoint
	 */
	public MyEngine(int arrivalMean, int arrivalVariance, int financeMean, int financeVariance,
					int testdriveMean, int testdriveVariance, int closureMean, int closureVariance,
					int simulationSpeed, ArrayList<String[]> carsToBeCreated, int arrivalServicePoints,
					int financeServicePoints, int testdriveServicePoints, int closureServicePoints, int customerArrivalInterval) {
		this.carDealerShop = new CarDealerShop();
		this.carsToBeCreated = carsToBeCreated;
		this.arrivalMean = arrivalMean;
		this.arrivalVariance = arrivalVariance;
		this.financeMean = financeMean;
		this.financeVariance = financeVariance;
		this.testdriveMean = testdriveMean;
		this.testdriveVariance = testdriveVariance;
		this.closureMean = closureMean;
		this.closureVariance = closureVariance;
		this.simulationSpeed = simulationSpeed;
		this.arrivalServicePoints = arrivalServicePoints;
		this.financeServicePoints = financeServicePoints;
		this.testdriveServicePoints = testdriveServicePoints;
		this.closureServicePoints = closureServicePoints;
		this.totalServicePoints = arrivalServicePoints + financeServicePoints + testdriveServicePoints + closureServicePoints;
		servicePoints = new ServicePoint[totalServicePoints];

		// Create cars
		carsToBeCreated(carsToBeCreated);
		carDealerShop.setMeanCarSalesProbability();
		double salesProbabilty = carDealerShop.getMeanCarSalesProbability();
		Trace.out(Trace.Level.INFO, "Cars at the beginning of the simulation: " + carDealerShop.getCarCollection().size());
		Trace.out(Trace.Level.INFO, "SalesProbability: " + salesProbabilty);

		if (TEXTDEMO) {
			// Special setup for text example
			r = new Random();

			ContinuousGenerator arrivalTime = null;
			if (FIXEDARRIVALTIMES) {
				// Fixed customer arrival times
				arrivalTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 10;
					}

					@Override
					public void setSeed(long seed) {
					}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {
					}
				};
			} else {
				// Exponential distribution for variable customer arrival times
				arrivalTime = new Negexp(10, Integer.toUnsignedLong(r.nextInt()));
			}
			if (FIXEDSERVICETIMES) {
				// Fixed service times
				serviceTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 9;
					}

					@Override
					public void setSeed(long seed) {
					}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {
					}
				};
			} else {
				// Normal distribution for variable service times
				arrivalServiceTime = new Normal(arrivalMean, arrivalVariance, seed);
				financeServiceTime = new Normal(financeMean, financeVariance, seed);
				testdriveServiceTime = new Normal(testdriveMean, testdriveVariance, seed);
				closureServiceTime = new Normal(closureMean, closureVariance, seed);
			}
			createArrivalServicePoints(arrivalServicePoints, arrivalServiceTime, eventList);
			createFinanceServicePoints(financeServicePoints, financeServiceTime, eventList);
			createTestdriveServicePoints(testdriveServicePoints, testdriveServiceTime, eventList, carDealerShop);
			createClosureServicePoints(closureServicePoints, closureServiceTime, eventList, carDealerShop);

			arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);
		} else {
			arrivalServiceTime = new Normal(arrivalMean, arrivalVariance, seed);
			financeServiceTime = new Normal(financeMean, financeVariance, seed);
			testdriveServiceTime = new Normal(testdriveMean, testdriveVariance, seed);
			closureServiceTime = new Normal(closureMean, closureVariance, seed);
			// Realistic simulation with variable arrival and service times
			createArrivalServicePoints(arrivalServicePoints, arrivalServiceTime, eventList);
			createFinanceServicePoints(financeServicePoints, financeServiceTime, eventList);
			createTestdriveServicePoints(testdriveServicePoints, testdriveServiceTime, eventList, carDealerShop);
			createClosureServicePoints(closureServicePoints, closureServiceTime, eventList, carDealerShop);

			arrivalProcess = new ArrivalProcess(new Negexp(15, new Random().nextLong()), eventList, EventType.ARR1);
		}
	}

	@Override
	protected void initialize() { // First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	@Override
	// Creates new customers and moves them through each servicePoint if possible
	protected void runEvent(Event t) {// B phase events
		Customer customer;
		ArrayList<ServicePoint> arrivalServicePoints = getServicePointsByEventType(EventType.DEP1);
		ArrayList<ServicePoint> financeServicePoints = getServicePointsByEventType(EventType.DEP2);
		ArrayList<ServicePoint> testdriveServicePoints = getServicePointsByEventType(EventType.DEP3);
		ArrayList<ServicePoint> closureServicePoints = getServicePointsByEventType(EventType.DEP4);
		ServicePoint shortestArrivalServicePoint = null;
		ServicePoint shortestFinanceServicePoint = null;
		ServicePoint shortestTestdriveServicePoint = null;
		ServicePoint shortestClosureServicePoint = null;
		ServicePoint longestArrivalServicePoint = null;
		ServicePoint longestFinanceServicePoint = null;
		ServicePoint longestTestdriveServicePoint = null;
		ServicePoint longestClosureServicePoint = null;
		Trace.out(Trace.Level.INFO, "ArrivalServicePoints: " + arrivalServicePoints.size());
		Trace.out(Trace.Level.INFO, "FinanceServicePoints: " + financeServicePoints.size());
		Trace.out(Trace.Level.INFO, "TestdriveServicePoints: " + testdriveServicePoints.size());
		Trace.out(Trace.Level.INFO, "ClosureServicePoints: " + closureServicePoints.size());
		Trace.out(Trace.Level.INFO, "Current simulation speed " + getSimulationSpeed());

		switch ((EventType) t.getType()) {
			case ARR1:
				// Loop through arrivalServicePoints and get the shortest queue. Add the customer there
				shortestArrivalServicePoint = getShortestQueue(arrivalServicePoints);
				shortestArrivalServicePoint.addQueue(new Customer());
				// Generate one arrival event per each customer added to the arrivalServicePoint queue. In this case one
				arrivalProcess.generateNextEvent();
				break;

			case DEP1:
				// Loop through each arrivalServicePoint
				for (ServicePoint arrivalPoint : arrivalServicePoints){
					// Get the financeServicePoint with the shortest queue for each iteration
					shortestFinanceServicePoint = getShortestQueue(financeServicePoints);
					// Retrieve the customer from there but don't remove it
					customer = arrivalPoint.peekQueue();
					// If there is a customer remove it from the arrivalServicePoint and add it to financeServicePoint
					if (customer != null){
						customer = arrivalPoint.removeQueue();
						shortestFinanceServicePoint.addQueue(customer);
					}
				}
				break;
			case DEP2:
				// Loop through each financeServicePoint
				for (ServicePoint financePoint : financeServicePoints) {
					// Get the test-driveServicePoint with the shortest queue for each iteration
					shortestTestdriveServicePoint = getShortestQueue(testdriveServicePoints);
					// Retrieve the customer from there but don't remove it
					customer = financePoint.peekQueue();
					// If there is a customer
					// And customer was granted financing
					// Remove it from the financeServicePoint and add it to test-driveServicePoint
					if (customer != null) {
						if (customer.isFinanceAccepted()) {
							customer = financePoint.removeQueue();
							shortestTestdriveServicePoint.addQueue(customer);
						}
					}
				}
				break;
			case DEP3:
				// Loop through test-driveServicePoints
				for (ServicePoint testdrive : testdriveServicePoints) {
					// Get the closureServicePoint with the shortest queue for each iteration
					shortestClosureServicePoint = getShortestQueue(closureServicePoints);
					// Retrieve the customer from there but don't remove it
					customer = testdrive.peekQueue();
					// If there is a customer
					// And customer is happy with his test-drive
					// Remove it from the test-driveServicePoint and add it to closureServicePoint
					if (customer != null) {
						if (customer.happyWithTestdrive()) {
							customer = testdrive.removeQueue();
							shortestClosureServicePoint.addQueue(customer);
						}
					}
				}
				break;

			case DEP4:
				// Loop through closureServicePoints
				for (ServicePoint closurePoint : closureServicePoints) {
					// Retrieve the customer from there but don't remove it
					customer = closurePoint.peekQueue();
					// If there is a customer
					// Remove it from the closureServicePoint
					// Add it to the processed list
					// Set removal time, total time and report results for the customer
					if (customer != null) {
						customer = closurePoint.removeQueue();
						processedCustomers.add(customer);
						customer.setRemovalTime(Clock.getInstance().getClock());
						customer.setTotalTime();
						customer.reportResults();
						break;
					}
				}
		}
		// Delay each iteration of the simulation by 1 second
		// User can speed up or slow down the simulation from different thread
		try {
			Thread.sleep(getSimulationSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	// Checks all the servicePoints in the simulation and begins service is possible
	protected void tryCEvents() {
		// Loop through each servicePoint in servicePoints
		for (ServicePoint point : servicePoints) {
			// If servicePoint is free and there is a queue
			// Begin the service
			if (!point.isReserved() && point.isOnQueue()) {
				point.beginService();
			}
		}
	}

	@Override
	protected void results() {
		System.out.println("Simulation ended at " + Clock.getInstance().getClock());
		System.out.println("Processed Customers: " + processedCustomers.size());

		// Initialize containers for all car and fuel types
		Set<String> allCarTypes = new HashSet<>();
		Set<String> allFuelTypes = new HashSet<>();

		// Collect all unique car and fuel types
		for (Customer customer : processedCustomers) {
			allCarTypes.add(customer.getPreferredCarType());
			allFuelTypes.add(customer.getPreferredFuelType());
		}

		// Print header and customer data
		System.out.println(String.format(
				"%-10s %-15s %-15s %-15s %-20s %-15s %-10s %-12s %-18s %-22s %-20s",
				"ID", "Arrival Time", "Removal Time", "Total time", "Preferred Car Type", "Fuel Type",
				"Budget", "Credit Score", "Finance Accepted", "Happy with Test-drive", "Purchased a Car"
		));
		double totalArrivalTime = 0, totalRemovalTime = 0, totalTime = 0, totalBudget = 0, totalCreditScore = 0;
		int totalCustomers = processedCustomers.size();
		int purchasedCount = 0;

		for (Customer customer : processedCustomers) {
			totalArrivalTime += customer.getArrivalTime();
			totalRemovalTime += customer.getRemovalTime();
			totalTime += customer.getTotalTime();
			totalBudget += customer.getBudget();
			totalCreditScore += customer.getCreditScore();
			if (customer.isPurchased()) purchasedCount++;

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

		// Print averages
		System.out.println(String.format(
				"%-10s %-15.2f %-15.2f %-15.2f %-20s %-15s %-10.2f %-12.2f %-18s %-22s %-20.2f",
				"AVG",
				totalArrivalTime / totalCustomers,
				totalRemovalTime / totalCustomers,
				totalTime / totalCustomers,
				"N/A",
				"N/A",
				totalBudget / totalCustomers,
				totalCreditScore / totalCustomers,
				"1.00",
				"1.00",
				purchasedCount / (double) totalCustomers
		));

		// Initialize maps for sold counts
		Map<String, Integer> carTypeSoldCounts = new HashMap<>();
		Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
		Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
		int totalSoldCars = 0;

		// Initialize counts to 0 for all car and fuel types
		for (String carType : allCarTypes) {
			carTypeSoldCounts.put(carType, 0);
		}
		for (String fuelType : allFuelTypes) {
			fuelTypeSoldCounts.put(fuelType, 0);
		}
		for (String carType : allCarTypes) {
			for (String fuelType : allFuelTypes) {
				carFuelTypeSoldCounts.put(carType + " - " + fuelType, 0);
			}
		}

		// Count sold cars
		for (Customer customer : processedCustomers) {
			if (customer.isPurchased()) {
				totalSoldCars++;
				String carType = customer.getPreferredCarType();
				String fuelType = customer.getPreferredFuelType();

				carTypeSoldCounts.put(carType, carTypeSoldCounts.get(carType) + 1);
				fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.get(fuelType) + 1);
				carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.get(carType + " - " + fuelType) + 1);
			}
		}

		// Print car type preferences
		System.out.println("\nCar Type Preferences (All Customers):");
		for (String carType : allCarTypes) {
			long count = processedCustomers.stream()
					.filter(customer -> customer.getPreferredCarType().equals(carType))
					.count();
			System.out.println(carType + ": " + String.format("%.2f", (count / (double) totalCustomers) * 100) + "%");
		}

		// Print fuel type preferences
		System.out.println("\nFuel Type Preferences (All Customers):");
		for (String fuelType : allFuelTypes) {
			long count = processedCustomers.stream()
					.filter(customer -> customer.getPreferredFuelType().equals(fuelType))
					.count();
			System.out.println(fuelType + ": " + String.format("%.2f", (count / (double) totalCustomers) * 100) + "%");
		}

		// Print sold percentages by car type
		System.out.println("\nCar Type Sold Percentages:");
		for (String carType : allCarTypes) {
			int soldCount = carTypeSoldCounts.getOrDefault(carType, 0);
			System.out.println(carType + ": " + String.format("%.2f", (soldCount / (double) totalSoldCars) * 100) + "%");
		}

		// Print sold percentages by fuel type
		System.out.println("\nFuel Type Sold Percentages:");
		for (String fuelType : allFuelTypes) {
			int soldCount = fuelTypeSoldCounts.getOrDefault(fuelType, 0);
			System.out.println(fuelType + ": " + String.format("%.2f", (soldCount / (double) totalSoldCars) * 100) + "%");
		}

		// Print sold percentages by car-fuel combinations
		System.out.println("\nCar Type and Fuel Type Combination Sold Percentages:");
		for (String combination : carFuelTypeSoldCounts.keySet()) {
			int soldCount = carFuelTypeSoldCounts.get(combination);
			System.out.println(combination + ": " + String.format("%.2f", (soldCount / (double) totalSoldCars) * 100) + "%");
		}

		// Print remaining cars and sold cars
		System.out.println("Cars left at the dealershop: " + carDealerShop.getCarCollection().size());
		System.out.println("Cars sold: " + totalSoldCars);
	}





	// Creates the amount of cars User asks for
	public void carsToBeCreated(ArrayList<String[]> carsToBeCreated) {
		int carType;
		int amount;
		int fuelType;
		double meanPrice;
		double variance;
		// For each car in carsToBeCreated
		// Retrieve the carType, amount, fuelType, meanPrice, variance
		// Calls callDealerShop's method and passes the arguments
		// callDealerShop creates the cars and adds them to it's collection
		for (String[] car : carsToBeCreated) {
			carType = Integer.parseInt(car[0]);
			amount = Integer.parseInt(car[1]);
			fuelType = Integer.parseInt(car[2]);
			meanPrice = Double.parseDouble(car[3]);
			variance = Double.parseDouble(car[4]);
			carDealerShop.createCar(amount, carType, fuelType, meanPrice, variance);
		}
	}

	public static void addProcessedCustomer(Customer customer) {
		if (customer != null) {
			processedCustomers.add(customer);
		}
	}

	public int getArrivalMean() {
		return arrivalMean;
	}

	public void setArrivalMean(int arrivalMean) {
		this.arrivalMean = arrivalMean;
	}

	public int getFinanceMean() {
		return financeMean;
	}

	public void setFinanceMean(int financeMean) {
		this.financeMean = financeMean;
	}

	public int getTestdriveMean() {
		return testdriveMean;
	}

	public void setTestdriveMean(int testdriveMean) {
		this.testdriveMean = testdriveMean;
	}

	public int getClosureMean() {
		return closureMean;
	}

	public void setClosureMean(int closureMean) {
		this.closureMean = closureMean;
	}

	public synchronized int getSimulationSpeed() {
		return simulationSpeed;
	}

	public synchronized void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
	}

	public synchronized int slowDownSimulationSpeed(int increment) {
		simulationSpeed += increment;
		return simulationSpeed;
	}

	public synchronized int speedUpSimulationSpeed(int decrement) {
		if (simulationSpeed <= 0) {
			simulationSpeed = 0;
			return simulationSpeed;
		}
		simulationSpeed -= decrement;

		return simulationSpeed;
	}
	// Creates arrivalServicePoints
	// Populates the servicePoints list by using a global currentServicePointIndex
	// This makes possible the creation of different servicePoints by different methods
	// And each servicePoint type is next to its own type
	// For example (arrivals, finances, test-drives, closures)
	public void createArrivalServicePoints(int amount, ContinuousGenerator arrivalServiceTime, EventList eventList) {
		for (int i = 0; i < amount; i++) {
			if (currentServicePointIndex < servicePoints.length) {
				servicePoints[currentServicePointIndex] = new ArrivalServicePoint(arrivalServiceTime, eventList, EventType.DEP1);
				currentServicePointIndex++; // Move to the next free index
			} else {
				throw new IllegalStateException("ServicePoints array is full. Cannot add more service points.");
			}
		}
	}

	// Same in here for financeServicePoints as in arrivalServicePointCreation
	public void createFinanceServicePoints(int amount, ContinuousGenerator financeServiceTime, EventList eventList) {
		for (int i = 0; i < amount; i++) {
			if (currentServicePointIndex < servicePoints.length) {
				servicePoints[currentServicePointIndex] = new FinanceServicePoint(financeServiceTime, eventList, EventType.DEP2);
				currentServicePointIndex++; // Move to the next free index
			} else {
				throw new IllegalStateException("ServicePoints array is full. Cannot add more service points.");
			}
		}
	}

	// Same in here for test-driveServicePoints as in arrivalServicePointCreation
	public void createTestdriveServicePoints(int amount, ContinuousGenerator testdriveServiceTime, EventList eventList, CarDealerShop carDealerShop) {
		for (int i = 0; i < amount; i++) {
			if (currentServicePointIndex < servicePoints.length) {
				servicePoints[currentServicePointIndex] = new TestdriveServicePoint(testdriveServiceTime, eventList, EventType.DEP3, carDealerShop);
				currentServicePointIndex++; // Move to the next free index
			} else {
				throw new IllegalStateException("ServicePoints array is full. Cannot add more service points.");
			}
		}
	}

	// Same in here for closureServicePoints as in arrivalServicePointCreation
	public void createClosureServicePoints(int amount, ContinuousGenerator closureServiceTime, EventList eventList, CarDealerShop carDealerShop) {
		for (int i = 0; i < amount; i++) {
			if (currentServicePointIndex < servicePoints.length) {
				servicePoints[currentServicePointIndex] = new ClosureServicePoint(closureServiceTime, eventList, EventType.DEP4, carDealerShop);
				currentServicePointIndex++; // Move to the next free index
			} else {
				throw new IllegalStateException("ServicePoints array is full. Cannot add more service points.");
			}
		}
	}

	// Loop through all servicePoints and retrieve the servicePoints with matching eventType
	// Event Type is passed as an argument
	public ArrayList<ServicePoint> getServicePointsByEventType(EventType type) {
		ArrayList<ServicePoint> filteredPoints = new ArrayList<>();
		for (ServicePoint p : servicePoints) {
			if (p.getEventType() == type) {
				filteredPoints.add(p);
			}
		}
		return filteredPoints;
	}

	// Get the servicePoint with the shortest queue
	// ServicePoints are passed as an argument
	private ServicePoint getShortestQueue(ArrayList<ServicePoint> points) {
		if (points == null || points.isEmpty()) {
			return null; // No points to evaluate
		}

		ServicePoint shortestServicePoint = points.get(0); // Assume first point is shortest initially

		for (ServicePoint point : points) {
			if (point.getQueue().size() < shortestServicePoint.getQueue().size()) {
				shortestServicePoint = point; // Update shortest point
			}
		}

		return shortestServicePoint;
	}

	// Get the servicePoint with the longest queue
	// ServicePoints are passed as an argument
	private ServicePoint getLongestQueue(ArrayList<ServicePoint> points) {
		if (points == null || points.isEmpty()) {
			return null; // No points to evaluate
		}

		ServicePoint longestServicePoint = points.get(0); // Assume first point has the longest queue initially

		for (ServicePoint point : points) {
			if (point.getQueue().size() > longestServicePoint.getQueue().size()) {
				longestServicePoint = point; // Update if a longer queue is found
			}
		}

		return longestServicePoint;
	}






	/*public synchronized void pause() {
		paused = !paused;
	}

	public synchronized boolean isPaused() {
		return paused;
	}*/
}
