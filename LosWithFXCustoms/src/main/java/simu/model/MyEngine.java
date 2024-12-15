package simu.model;

import java.util.*;
import simu.framework.*;
import eduni.distributions.Normal;
import eduni.distributions.Negexp;
import eduni.distributions.ContinuousGenerator;
import simu.model.servicepoints.ArrivalServicePoint;
import simu.model.servicepoints.ClosureServicePoint;
import simu.model.servicepoints.FinanceServicePoint;
import simu.model.servicepoints.TestdriveServicePoint;

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
	private int currentServicePointIndex;// Tracks the next free index
	private final int seed = 123;
	private final double ARRIVALCOEFFICIENT = 100;
	private double arrivalInterval;
	ContinuousGenerator arrivalServiceTime = null;
	ContinuousGenerator financeServiceTime = null;
	ContinuousGenerator testdriveServiceTime = null;
	ContinuousGenerator closureServiceTime = null;
	Random r = null;



	public MyEngine() {
		r = new Random();
		this.carDealerShop = new CarDealerShop();
		currentServicePointIndex = 0;
	}

	@Override
	protected void initialize() { // First arrival in the system
		arrivalProcess.generateNextEvent();
	}


	/**
	 * Processes a simulation event and manages customer interactions with service points.
	 *
	 * <p><strong>Key Steps:</strong></p>
	 * <ul>
	 *   <li>Processes arrival events to add customers to the system.</li>
	 *   <li>Handles departure events from each service point (arrival, finance, test-drive, closure).</li>
	 *   <li>Tracks customer journey metrics such as wait times and service preferences.</li>
	 * </ul>
	 *
	 * @param t The simulation event to be processed. Must not be null.
	 * @throws InterruptedException If the thread sleep during simulation delay is interrupted.
	 */
	@Override
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
		Trace.out(Trace.Level.INFO, "ArrivalServicePoints: " + arrivalServicePoints.size());
		Trace.out(Trace.Level.INFO, "FinanceServicePoints: " + financeServicePoints.size());
		Trace.out(Trace.Level.INFO, "TestdriveServicePoints: " + testdriveServicePoints.size());
		Trace.out(Trace.Level.INFO, "ClosureServicePoints: " + closureServicePoints.size());
		Trace.out(Trace.Level.INFO, "Current simulation speed " + getSimulationSpeed());

		switch ((EventType) t.getType()) {
			case ARR1:
				// Loop through arrivalServicePoints and get the shortest queue. Add the customer there
				shortestArrivalServicePoint = getShortestQueue(arrivalServicePoints);
				customer = new Customer();
				shortestArrivalServicePoint.addQueue(customer);
				customer.setArrivalTimeAtArrivalServicePoint(Clock.getInstance().getClock());
				carDealerShop.addCustomerAtTheDealership(customer);
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
						customer.setRemovalTimeAtArrivalServicePoint(Clock.getInstance().getClock());
						customer.updateTotalTimeAtArrivalServicePoint();
						shortestFinanceServicePoint.addQueue(customer);
						customer.setArrivalTimeAtFinanceServicePoint(Clock.getInstance().getClock());
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
							customer.setRemovalTimeAtFinanceServicePoint(Clock.getInstance().getClock());
							customer.updateTotalTimeAtFinanceServicePoint();
							shortestTestdriveServicePoint.addQueue(customer);
							customer.setArrivalTimeAtTestDriveServicePoint(Clock.getInstance().getClock());
						} else {
							customer = financePoint.removeQueue();//// Remove customer
							customer.setRemovalTimeAtFinanceServicePoint(Clock.getInstance().getClock());
							customer.updateTotalTimeAtFinanceServicePoint();
							customer.setRemovalTime(Clock.getInstance().getClock());
							customer.setTotalTime();
							MyEngine.addProcessedCustomer(customer); // Add to the processed list
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
							customer.setRemovalTimeAtTestDriveServicePoint(Clock.getInstance().getClock());
							customer.updateTotalTimeAtTestDriveServicePoint();
							shortestClosureServicePoint.addQueue(customer);
							customer.setArrivalTimeAtClosureServicePoint(Clock.getInstance().getClock());
						} else {
							customer = testdrive.removeQueue();//// Remove customer
							customer.setRemovalTimeAtTestDriveServicePoint(Clock.getInstance().getClock());
							customer.updateTotalTimeAtTestDriveServicePoint();
							customer.setRemovalTime(Clock.getInstance().getClock());
							customer.setTotalTime();
							MyEngine.addProcessedCustomer(customer); // Add to the processed list
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
						customer.setRemovalTimeAtClosureServicePoint(Clock.getInstance().getClock());
						customer.updateTotalTimeAtClosureServicePoint();
						carDealerShop.removeCustomerAtTheDealership(customer);
						processedCustomers.add(customer);
						customer.setRemovalTime(Clock.getInstance().getClock());
						customer.setTotalTime();
						customer.reportResults();
					}
				}
				break;
		}
		// Delay each iteration of the simulation by 1 second
		// User can speed up or slow down the simulation from different thread
		try {
			Thread.sleep(getSimulationSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Attempts to initiate service at all service points that have a queue
	 * and are not currently reserved. This method is executed in each simulation cycle.
	 */
	@Override
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


	/**
	 * Displays the results of the simulation in the console.
	 *
	 * <p>This method outputs detailed statistics about the simulation, including:</p>
	 * <ul>
	 *   <li>Processed customer details: arrival/removal times, service durations, preferences, and outcomes.</li>
	 *   <li>Average metrics across all customers (e.g., time spent, budget, credit scores).</li>
	 *   <li>Car and fuel type preferences for all customers.</li>
	 *   <li>Sales statistics, including percentages for car types, fuel types, and combinations.</li>
	 *   <li>Remaining cars and total cars sold during the simulation.</li>
	 * </ul>
	 *
	 * <p><strong>Note:</strong> Results are displayed in a tabular format for readability.</p>
	 */
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


	/**
	 * Initializes the car inventory for the simulation.
	 *
	 * <p>This method generates cars based on the provided list of parameters and adds them
	 * to the <code>CarDealerShop</code>. Each car is initialized with its type, fuel type,
	 * price, variance, and optional base price.</p>
	 *
	 * @param carsToBeCreated A list of car specifications where each entry contains:
	 *                        <ol>
	 *                          <li>Amount of cars to create</li>
	 *                          <li>Car type (e.g., sedan, SUV)</li>
	 *                          <li>Fuel type (e.g., petrol, electric)</li>
	 *                          <li>Mean price</li>
	 *                          <li>Variance in price</li>
	 *                          <li>Base price (optional)</li>
	 *                        </ol>
	 * @throws NumberFormatException If any numeric value in the input is invalid.
	 */
	public void carsToBeCreated(ArrayList<String[]> carsToBeCreated) {
		String carType;
		int amount;
		String fuelType;
		double meanPrice;
		double variance;
		double basePrice;
		// For each car in carsToBeCreated
		// Retrieve the carType, amount, fuelType, meanPrice, variance
		// Calls callDealerShop's method and passes the arguments
		// callDealerShop creates the cars and adds them to it's collection
		for (String[] car : carsToBeCreated) {
			if (car.length >= 5) {
				amount = Integer.parseInt(car[0]);
				carType = car[1];
				fuelType = car[2];
				meanPrice = Double.parseDouble(car[3]);
				variance = Double.parseDouble(car[4]);
				basePrice = Double.parseDouble(car[5]);
				carDealerShop.createCar(amount, carType, fuelType, meanPrice, variance, basePrice);

			} else {
				amount = Integer.parseInt(car[0]);
				carType = car[1];
				fuelType = car[2];
				meanPrice = Double.parseDouble(car[3]);
				variance = Double.parseDouble(car[4]);
				carDealerShop.createCar(amount, carType, fuelType, meanPrice, variance);
			}
		}
		Trace.out(Trace.Level.INFO, "Cars at the beginning of the simulation: " + carDealerShop.getCarCollection().size());
	}
	/**
	 * @hidden
	 */
	public static void addProcessedCustomer(Customer customer) {
		if (customer != null && !processedCustomers.contains(customer)) {
			processedCustomers.add(customer);
		}
	}
	/**
	 * @hidden
	 */
	public EventList getEventList() {
		return eventList;
	}

	public void setArrivalProcess(double arrivalInterval, EventList eventList) {
		Random r = new Random();
		ContinuousGenerator arrivalTime = new Negexp(arrivalInterval, Integer.toUnsignedLong(r.nextInt()));
		this.arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);
		Trace.out(Trace.Level.INFO, "arrivalTime: " + arrivalTime.sample() + " arrivalinterval: " + arrivalInterval + " eventlist: " + eventList + "eventype: " + EventType.ARR1 );
	}
	/**
	 * @hidden
	 */
	public void setServicePoints(int totalServicePoints) {
		this.servicePoints = new ServicePoint[totalServicePoints];
	}
	/**
	 * @hidden
	 */
	public ArrayList<Customer> getProcessedCustomer(){
		return processedCustomers;
	}
	/**
	 * @hidden
	 */
	public CarDealerShop getCarDealerShop() {
		return carDealerShop;
	}
	/**
	 * @hidden
	 */
	public int getArrivalMean() {
		return arrivalMean;
	}
	/**
	 * @hidden
	 */
	public void setArrivalMean(int arrivalMean) {
		this.arrivalMean = arrivalMean;
	}
	/**
	 * @hidden
	 */
	public int getArrivalVariance() {
		return arrivalVariance;
	}
	/**
	 * @hidden
	 */
	public void setArrivalVariance(int arrivalVariance) {
		this.arrivalVariance = arrivalVariance;
	}
	/**
	 * @hidden
	 */
	public int getFinanceMean() {
		return financeMean;
	}
	/**
	 * @hidden
	 */
	public void setFinanceMean(int financeMean) {
		this.financeMean = financeMean;
	}
	/**
	 * @hidden
	 */
	public int getFinanceVariance() {
		return financeVariance;
	}
	/**
	 * @hidden
	 */
	public void setFinanceVariance(int financeVariance) {
		this.financeVariance = financeVariance;
	}
	/**
	 * @hidden
	 */
	public int getTestdriveMean() {
		return testdriveMean;
	}
	/**
	 * @hidden
	 */
	public void setTestdriveMean(int testdriveMean) {
		this.testdriveMean = testdriveMean;
	}
	/**
	 * @hidden
	 */
	public int getTestdriveVariance() {
		return testdriveVariance;
	}
	/**
	 * @hidden
	 */
	public void setTestdriveVariance(int testdriveVariance) {
		this.testdriveVariance = testdriveVariance;
	}
	/**
	 * @hidden
	 */
	public int getClosureMean() {
		return closureMean;
	}
	/**
	 * @hidden
	 */
	public void setClosureMean(int closureMean) {
		this.closureMean = closureMean;
	}
	/**
	 * @hidden
	 */
	public int getClosureVariance() {
		return closureVariance;
	}
	/**
	 * @hidden
	 */
	public void setClosureVariance(int closureVariance) {
		this.closureVariance = closureVariance;
	}
	/**
	 * @hidden
	 */
	public synchronized int getSimulationSpeed() {
		return simulationSpeed;
	}
	/**
	 * @hidden
	 */
	public synchronized void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
	}


	/**
	 * Creates service points for customer arrivals in the simulation.
	 *
	 * <p>This method initializes the specified number of arrival service points, configures
	 * them with the provided service time distribution, and associates them with the
	 * event list. The service points are added to the <code>servicePoints</code> array.</p>
	 *
	 * @param amount             The number of arrival service points to create.
	 * @param arrivalServiceTime A distribution generator for arrival service times.
	 * @param eventList          The simulation event list to associate with the service points.
	 * @throws IllegalStateException If the <code>servicePoints</code> array is full.
	 */
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
	/**
	 * @hidden
	 */
	public void setCurrentServicePointIndex(int amount){
		currentServicePointIndex = amount;
	}
	/**
	 * @hidden
	 */
	public void setArrivalServicePoints(int arrivalServicePoints) {
		this.arrivalServicePoints = arrivalServicePoints;
	}
	/**
	 * @hidden
	 */
	public int getArrivalServicePoints() {
		return arrivalServicePoints;
	}


	/**
	 * Creates finance service points for the simulation. Each point is added to the
	 * <code>servicePoints</code> array, and the current service point index is updated.
	 *
	 * @param amount             Number of finance service points to create.
	 * @param financeServiceTime Distribution for finance service times.
	 * @param eventList          List of simulation events.
	 */
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
	/**
	 * @hidden
	 */
	public void setFinanceServicePoints(int financeServicePoints) {
		this.financeServicePoints = financeServicePoints;
	}
	/**
	 * @hidden
	 */
	public int getFinanceServicePoints() {
		return financeServicePoints;
	}


	/**
	 * Creates test-drive service points for the simulation. Each point is added to the
	 * <code>servicePoints</code> array, and the current service point index is updated.
	 *
	 * @param amount                Number of test-drive service points to create.
	 * @param testdriveServiceTime  Distribution for test-drive service times.
	 * @param eventList             List of simulation events.
	 * @param carDealerShop         Reference to the car dealer shop for accessing cars.
	 */
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
	/**
	 * @hidden
	 */
	public void setTestdriveServicePoints(int testdriveServicePoints) {
		this.testdriveServicePoints = testdriveServicePoints;
	}
	/**
	 * @hidden
	 */
	public int getTestdriveServicePoints() {
		return testdriveServicePoints;
	}


	/**
	 * Creates closure service points for the simulation.
	 *
	 * <p>This method initializes the specified number of closure service points. Each service
	 * point is configured with the provided service time distribution, event list, and a reference
	 * to the <code>CarDealerShop</code>. The created service points are added to the
	 * <code>servicePoints</code> array.</p>
	 *
	 * @param amount             The number of closure service points to create.
	 * @param closureServiceTime A distribution generator for closure service times.
	 * @param eventList          The simulation event list to associate with the service points.
	 * @param carDealerShop      A reference to the car dealership for accessing car-related data.
	 * @throws IllegalStateException If the <code>servicePoints</code> array is full.
	 */
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
	/**
	 * @hidden
	 */
	public void setClosureServicePoints(int closureServicePoints) {
		this.closureServicePoints = closureServicePoints;
	}
	/**
	 * @hidden
	 */
	public int getClosureServicePoints() {
		return closureServicePoints;
	}
	/**
	 * @hidden
	 */
	public int getTotalServicePoints() {
		return totalServicePoints;
	}
	/**
	 * @hidden
	 */
	public void setTotalServicePoints(int arrivalServicePoints, int financeServicePoints, int testdriveServicePoints, int closureServicePoints) {
		this.totalServicePoints = arrivalServicePoints + financeServicePoints + testdriveServicePoints + closureServicePoints;
	}
	/**
	 * @hidden
	 */
	public double getArrivalInterval() {
		return arrivalInterval;
	}
	/**
	 * @hidden
	 */
	public void setArrivalInterval(double salesProbabilty) {
		this.arrivalInterval = Math.max(1, (int) (ARRIVALCOEFFICIENT * salesProbabilty));
		Trace.out(Trace.Level.INFO, "SalesProbability: " + salesProbabilty);
	}
	/**
	 * @hidden
	 */
	public ContinuousGenerator getArrivalServiceTime() {
		return arrivalServiceTime;
	}
	/**
	 * @hidden
	 */
	public void setArrivalServiceTime(int arrivalMean, int arrivalVariance) {
		this.arrivalServiceTime = new Normal(arrivalMean, arrivalVariance, seed);
	}
	/**
	 * @hidden
	 */
	public ContinuousGenerator getFinanceServiceTime() {
		return financeServiceTime;
	}
	/**
	 * @hidden
	 */
	public void setFinanceServiceTime(int financeMean, int financeVariance) {
		this.financeServiceTime = new Normal(financeMean, financeVariance, seed);
	}
	/**
	 * @hidden
	 */
	public ContinuousGenerator getTestdriveServiceTime() {
		return testdriveServiceTime;
	}
	/**
	 * @hidden
	 */
	public void setTestdriveServiceTime(int testdriveMean, int testdriveVariance) {
		this.testdriveServiceTime = new Normal(testdriveMean, testdriveVariance, seed);
	}
	/**
	 * @hidden
	 */
	public ContinuousGenerator getClosureServiceTime() {
		return closureServiceTime;
	}
	/**
	 * @hidden
	 */
	public void setClosureServiceTime(int closureMean, int closureVariance) {
		this.closureServiceTime = new Normal(closureMean, closureVariance, seed);
	}


	/**
	 * Retrieves all service points that match the specified event type.
	 *
	 * <p>This method filters the <code>servicePoints</code> array and returns a list of
	 * service points that handle the given event type. If no matching service points are
	 * found, an empty list is returned.</p>
	 *
	 * @param type The event type to filter service points by (e.g., ARRIVAL, FINANCE, TESTDRIVE, CLOSURE).
	 * @return A list of service points that handle the specified event type. Returns an empty list if no matches are found.
	 */
	public ArrayList<ServicePoint> getServicePointsByEventType(EventType type) {
		ArrayList<ServicePoint> filteredPoints = new ArrayList<>();
		for (ServicePoint p : servicePoints) {
			if (p.getEventType() == type) {
				filteredPoints.add(p);
			}
		}
		return filteredPoints;
	}


	/**
	 * Identifies the service point with the shortest queue from a given list.
	 *
	 * <p>This method evaluates the queue sizes of all service points in the provided list
	 * and returns the one with the shortest queue. If multiple service points have the same
	 * queue size, the first encountered is returned.</p>
	 *
	 * @param points A list of service points to evaluate. Must not be null.
	 * @return The service point with the shortest queue, or <code>null</code> if the list is empty.
	 */
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
}