package simu.model;

import simu.framework.*;
import java.util.Random;
import eduni.distributions.Normal;

/**
 * Represents a customer in the simulation.
 *
 * <p>This class models a customer's journey through the dealership simulation,
 * including their preferences, financial attributes, and interactions with service points.
 * Customers are assigned random attributes such as car type, fuel type, budget, and credit score
 * based on predefined distributions.</p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Tracks customer-specific attributes such as budget, credit score, and preferences.</li>
 *   <li>Records arrival and removal times for each service point.</li>
 *   <li>Calculates probabilities for financial approval and satisfaction with test drives.</li>
 *   <li>Maintains the customer's state (e.g., finance approval, test drive satisfaction).</li>
 * </ul>
 */
public class Customer {
	// Simulation-specific variables (from first class)
	private double arrivalTime;
	private double removalTime;
	private double totalTime;
	private int id;
	private static int i = 1;
	private static long sum = 0;

	// Customer-specific attributes (from second class)
	private String preferredCarType;
	private String preferredFuelType;
	private double budget;
	private int creditScore;
	private boolean financeAccepted = false;
	private boolean happyWithTestdrive = false;
	private Car purchaseCar;
	private boolean purchased = false;
	private double financeProb;
	private final double BASEPROB = 0.9;
	private final double BASECREDITSCORE = 500;
	private final double COEFFICIENT = 0.05;
	private int testDriveCount;
	private String currentServicePoint;
	private double arrivalTimeAtArrivalServicePoint = 0;
	private double arrivalTimeAtFinanceServicePoint = 0;
	private double arrivalTimeAtTestDriveServicePoint = 0;
	private double arrivalTimeAtClosureServicePoint = 0;
	private double removalTimeAtArrivalServicePoint = 0;
	private double removalTimeAtFinanceServicePoint = 0;
	private double removalTimeAtTestDriveServicePoint = 0;
	private double removalTimeAtClosureServicePoint = 0;
	private double totalTimeAtArrivalServicePoint = 0;
	private double totalTimeAtFinanceServicePoint = 0;
	private double totalTimeAtTestDriveServicePoint = 0;
	private double totalTimeAtClosureServicePoint = 0;


	// Normal distribution parameters for Budget and Credit Score
	// Will be user inputs later on
	private static final double BUDGET_MEAN = 40000.0;   // Mean budget: $30,000.
	private static final double BUDGET_STDDEV = 15000.0; // Standard deviation: $10,000
	private static final double CREDIT_SCORE_MEAN = 650; // Mean credit score: 650
	private static final double CREDIT_SCORE_STDDEV = 200;// Standard deviation: 100

	private Normal budgetNormalDistribution = new Normal(BUDGET_MEAN, BUDGET_STDDEV);
	private Normal creditNormalDistribution = new Normal(CREDIT_SCORE_MEAN, CREDIT_SCORE_STDDEV);

	private static final Random random = new Random();

	/**
	 * Constructs a new customer with randomly assigned attributes.
	 *
	 * <p>Each customer is initialized with the following attributes:</p>
	 * <ul>
	 *   <li>Unique ID assigned sequentially.</li>
	 *   <li>Arrival time based on the simulation clock.</li>
	 *   <li>Randomly generated budget and credit score from normal distributions.</li>
	 *   <li>Randomly assigned car type and fuel type based on predefined probabilities.</li>
	 * </ul>
	 *
	 * <p>Initial state:</p>
	 * <ul>
	 *   <li>Finance approval: <code>false</code></li>
	 *   <li>Test drive satisfaction: <code>false</code></li>
	 *   <li>Purchase status: <code>false</code></li>
	 * </ul>
	 */
	public Customer() {
		// Assign unique ID
		id = i++;

		// Record arrival time
		arrivalTime = Clock.getInstance().getClock();
		Trace.out(Trace.Level.INFO, "New customer #" + id + " arrived at " + arrivalTime);

		// Generate normally distributed budget and credit score
		this.budget = budgetNormalDistribution.sample();
		this.creditScore = (int) creditNormalDistribution.sample();

		// Ensure the credit score is within valid range (300-850)
		this.creditScore = Math.min(850, Math.max(300, this.creditScore));

		// Assign random car type and fuel type
		this.preferredCarType = assignCarType();
		this.preferredFuelType = assignFuelType();
		this.happyWithTestdrive = false;
		this.financeAccepted = false;
		this.purchased = false;
		this.testDriveCount = 1;
		currentServicePoint = "entry";
	}

	/**
	 * Assigns a random car type to the customer based on predefined probabilities.
	 *
	 * <p><strong>Probabilities:</strong></p>
	 * <ul>
	 *   <li>20% - SUV</li>
	 *   <li>20% - Sport</li>
	 *   <li>30% - Sedan</li>
	 *   <li>20% - Van</li>
	 *   <li>10% - Compact</li>
	 * </ul>
	 *
	 * @return A randomly assigned car type as a <code>String</code>.
	 */
	private String assignCarType() {
		double rand = Math.random();
		if (rand < 0.2) {
			return "SUV";
		} else if (rand < 0.4) {
			return "Sport";
		} else if (rand < 0.7) {
			return "Sedan";
		} else if (rand < 0.9) {
			return "Van";
		} else {
			return "Compact";
		}
	}

	/**
	 * Assigns a random fuel type to the customer based on predefined probabilities.
	 *
	 * <p><strong>Probabilities:</strong></p>
	 * <ul>
	 *   <li>50% - Gas</li>
	 *   <li>30% - Hybrid</li>
	 *   <li>20% - Electric</li>
	 * </ul>
	 *
	 * @return A randomly assigned fuel type as a <code>String</code>.
	 */
	private String assignFuelType() {
		double rand = Math.random();
		if (rand < 0.5) {
			return "gas";
		} else if (rand < 0.8) {
			return "hybrid";
		} else {
			return "electric";
		}
	}


	/**
	 * Logs and reports the customer's journey and statistics.
	 *
	 * <p>This method outputs details about the customer's experience, including:</p>
	 * <ul>
	 *   <li>Arrival and removal times.</li>
	 *   <li>Total time spent in the simulation.</li>
	 *   <li>Updated mean service time for all customers.</li>
	 * </ul>
	 *
	 * <p>The mean service time is updated incrementally for each customer.</p>
	 */
	public void reportResults() {
		Trace.out(Trace.Level.INFO, "\nCustomer #" + id + " ready!");
		Trace.out(Trace.Level.INFO, "Customer #" + id + " arrived at: " + arrivalTime);
		Trace.out(Trace.Level.INFO, "Customer #" + id + " removed at: " + removalTime);
		Trace.out(Trace.Level.INFO, "Customer #" + id + " stayed for: " + (removalTime - arrivalTime));

		// Update total service time and compute mean
		sum += (removalTime - arrivalTime);
		double meanServiceTime = sum / id;
		System.out.println("Current mean service time: " + meanServiceTime);
	}

	/**
	 * Calculates the probability of financial approval based on the customer's credit score.
	 *
	 * <p>If the customer's credit score meets or exceeds a baseline, a base probability
	 * is returned. Otherwise, the probability is reduced exponentially based on the difference
	 * between the credit score and the baseline score.</p>
	 *
	 * <p><strong>Formula:</strong></p>
	 * <code>financeProbability = BASEPROB * e^(-COEFFICIENT * (customerCreditScore - BASECREDITSCORE))</code>
	 *
	 * @param customerCreditScore The customer's credit score.
	 * @return The calculated probability of financial approval as a <code>double</code>.
	 */
	public double calculateFinanceProbability(double customerCreditScore) {
		if (customerCreditScore >= BASECREDITSCORE) {
			return BASEPROB;
		} else {
			financeProb = BASEPROB * Math.exp(-COEFFICIENT * (customerCreditScore - BASECREDITSCORE));
			return financeProb;
		}
	}


	// Getters and Setters for Simulation-Specific Variables

	/**
	 * @hidden
	 */
	public double getArrivalTime() {
		return arrivalTime;
	}
	/**
	 * @hidden
	 */
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	/**
	 * @hidden
	 */
	public double getRemovalTime() {
		return removalTime;
	}
	/**
	 * @hidden
	 */
	public void setRemovalTime(double removalTime) {
		this.removalTime = removalTime;
	}
	/**
	 * @hidden
	 */
	public double getTotalTime() {
		return totalTime;
	}
	/**
	 * @hidden
	 */
	public void setTotalTime() {
		totalTime = removalTime - arrivalTime;
	}
	/**
	 * @hidden
	 */
	public int getId() {
		return id;
	}


	// Getters for Customer Attributes

	/**
	 * @hidden
	 */
	public String getPreferredCarType() {
		return preferredCarType;
	}
	/**
	 * @hidden
	 */
	public String getPreferredFuelType() {
		return preferredFuelType;
	}
	/**
	 * @hidden
	 */
	public double getBudget() {
		return budget;
	}
	/**
	 * @hidden
	 */
	public int getCreditScore() {
		return creditScore;
	}
	/**
	 * @hidden
	 */
	public boolean isFinanceAccepted() {
		return financeAccepted;
	}
	/**
	 * @hidden
	 */
	public void setFinanceAccepted(boolean financeAccepted) {
		this.financeAccepted = financeAccepted;
	}
	/**
	 * @hidden
	 */
	public boolean happyWithTestdrive() {
		return happyWithTestdrive;
	}
	/**
	 * @hidden
	 */
	public void setHappyWithTestdrive(boolean happyWithTestdrive) {
		this.happyWithTestdrive = happyWithTestdrive;
	}
	/**
	 * @hidden
	 */
	public Car getPurchaseCar() {
		return purchaseCar;
	}
	/**
	 * @hidden
	 */
	public void setPurchaseCar(Car car) {
		this.purchaseCar = car;
	}
	/**
	 * @hidden
	 */
	public boolean isPurchased() {
		return purchased;
	}
	/**
	 * @hidden
	 */
	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}
	/**
	 * @hidden
	 */
	public double getBaseCreditScore() {
		return BASECREDITSCORE;
	}
	/**
	 * @hidden
	 */
	public void increaseTestDriveCount() {
		testDriveCount++;
	}
	/**
	 * @hidden
	 */
	public int getTestDriveCount() {
		return testDriveCount;
	}
	/**
	 * @hidden
	 */
	public String getCurrentServicePoint() {
		return currentServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setCurrentServicePoint(String currentServicePoint) {
		this.currentServicePoint = currentServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getArrivalTimeAtArrivalServicePoint() {
		return arrivalTimeAtArrivalServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setArrivalTimeAtArrivalServicePoint(double arrivalTimeAtArrivalServicePoint) {
		this.arrivalTimeAtArrivalServicePoint = arrivalTimeAtArrivalServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getArrivalTimeAtFinanceServicePoint() {
		return arrivalTimeAtFinanceServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setArrivalTimeAtFinanceServicePoint(double arrivalTimeAtFinanceServicePoint) {
		this.arrivalTimeAtFinanceServicePoint = arrivalTimeAtFinanceServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getArrivalTimeAtTestDriveServicePoint() {
		return arrivalTimeAtTestDriveServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setArrivalTimeAtTestDriveServicePoint(double arrivalTimeAtTestDriveServicePoint) {
		this.arrivalTimeAtTestDriveServicePoint = arrivalTimeAtTestDriveServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getArrivalTimeAtClosureServicePoint() {
		return arrivalTimeAtClosureServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setArrivalTimeAtClosureServicePoint(double arrivalTimeAtClosureServicePoint) {
		this.arrivalTimeAtClosureServicePoint = arrivalTimeAtClosureServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getRemovalTimeAtArrivalServicePoint() {
		return removalTimeAtArrivalServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setRemovalTimeAtArrivalServicePoint(double removalTimeAtArrivalServicePoint) {
		this.removalTimeAtArrivalServicePoint = removalTimeAtArrivalServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getRemovalTimeAtFinanceServicePoint() {
		return removalTimeAtFinanceServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setRemovalTimeAtFinanceServicePoint(double removalTimeAtFinanceServicePoint) {
		this.removalTimeAtFinanceServicePoint = removalTimeAtFinanceServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getRemovalTimeAtTestDriveServicePoint() {
		return removalTimeAtTestDriveServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setRemovalTimeAtTestDriveServicePoint(double removalTimeAtTestDriveServicePoint) {
		this.removalTimeAtTestDriveServicePoint = removalTimeAtTestDriveServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getRemovalTimeAtClosureServicePoint() {
		return removalTimeAtClosureServicePoint;
	}
	/**
	 * @hidden
	 */
	public void setRemovalTimeAtClosureServicePoint(double removalTimeAtClosureServicePoint) {
		this.removalTimeAtClosureServicePoint = removalTimeAtClosureServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getTotalTimeAtArrivalServicePoint() {
		return totalTimeAtArrivalServicePoint;
	}
	/**
	 * @hidden
	 */
	public void updateTotalTimeAtArrivalServicePoint() {
		this.totalTimeAtArrivalServicePoint = removalTimeAtArrivalServicePoint - arrivalTimeAtArrivalServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getTotalTimeAtFinanceServicePoint() {
		return totalTimeAtFinanceServicePoint;
	}
	/**
	 * @hidden
	 */
	public void updateTotalTimeAtFinanceServicePoint() {
		this.totalTimeAtFinanceServicePoint = removalTimeAtFinanceServicePoint - arrivalTimeAtFinanceServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getTotalTimeAtTestDriveServicePoint() {
		return totalTimeAtTestDriveServicePoint;
	}
	/**
	 * @hidden
	 */
	public void updateTotalTimeAtTestDriveServicePoint() {
		this.totalTimeAtTestDriveServicePoint = removalTimeAtTestDriveServicePoint - arrivalTimeAtTestDriveServicePoint;
	}
	/**
	 * @hidden
	 */
	public double getTotalTimeAtClosureServicePoint() {
		return totalTimeAtClosureServicePoint;
	}
	/**
	 * @hidden
	 */
	public void updateTotalTimeAtClosureServicePoint() {
		this.totalTimeAtClosureServicePoint = removalTimeAtClosureServicePoint - arrivalTimeAtClosureServicePoint;
	}

	/**
	 * Returns a string representation of the customer.
	 *
	 * <p>This method provides a detailed overview of the customer's key attributes,
	 * including their ID, arrival and removal times, car and fuel type preferences,
	 * budget, credit score, and status indicators such as finance acceptance and
	 * test drive satisfaction.</p>
	 *
	 * @return A string representation of the customer, formatted as:</br>
	 *         <code>Customer{id=..., arrivalTime=..., removalTime=..., ...}</code>
	 */
	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", arrivalTime=" + arrivalTime +
				", removalTime=" + removalTime +
				", preferredCarType='" + preferredCarType + '\'' +
				", preferredFuelType='" + preferredFuelType + '\'' +
				", budget=" + budget +
				", creditScore=" + creditScore +
				", isFinanceAccepted=" + financeAccepted +
				", happyWithTestdrive=" + happyWithTestdrive +
				'}';
	}
}
