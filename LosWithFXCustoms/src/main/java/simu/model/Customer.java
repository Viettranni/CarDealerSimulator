package simu.model;

import simu.framework.*;
import java.util.Random;
import eduni.distributions.Normal;

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

	// Constructor
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

	// Method to assign a random car type based on predefined probabilities (discrete distribution)
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

	// Method to assign a random fuel type based on predefined probabilities (discrete distribution)
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

	// Getters and Setters for Simulation-Specific Variables
	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getRemovalTime() {
		return removalTime;
	}

	public void setRemovalTime(double removalTime) {
		this.removalTime = removalTime;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime() {
		totalTime = removalTime - arrivalTime;
	}

	public int getId() {
		return id;
	}

	// Report results for this customer
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

	// Getters for Customer Attributes
	public String getPreferredCarType() {
		return preferredCarType;
	}

	public String getPreferredFuelType() {
		return preferredFuelType;
	}

	public double getBudget() {
		return budget;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public boolean isFinanceAccepted() {
		return financeAccepted;
	}

	public void setFinanceAccepted(boolean financeAccepted) {
		this.financeAccepted = financeAccepted;
	}

	public boolean happyWithTestdrive() {
		return happyWithTestdrive;
	}

	public void setHappyWithTestdrive(boolean happyWithTestdrive) {
		this.happyWithTestdrive = happyWithTestdrive;
	}

	public Car getPurchaseCar() {
		return purchaseCar;
	}

	public void setPurchaseCar(Car car) {
		this.purchaseCar = car;
	}

	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}

	public double getBaseCreditScore() {
		return BASECREDITSCORE;
	}

	public void increaseTestDriveCount() {
		testDriveCount++;
	}

	public int getTestDriveCount() {
		return testDriveCount;
	}

	public String getCurrentServicePoint() {
		return currentServicePoint;
	}

	public void setCurrentServicePoint(String currentServicePoint) {
		this.currentServicePoint = currentServicePoint;
	}

	public double getArrivalTimeAtArrivalServicePoint() {
		return arrivalTimeAtArrivalServicePoint;
	}

	public void setArrivalTimeAtArrivalServicePoint(double arrivalTimeAtArrivalServicePoint) {
		this.arrivalTimeAtArrivalServicePoint = arrivalTimeAtArrivalServicePoint;
	}

	public double getArrivalTimeAtFinanceServicePoint() {
		return arrivalTimeAtFinanceServicePoint;
	}

	public void setArrivalTimeAtFinanceServicePoint(double arrivalTimeAtFinanceServicePoint) {
		this.arrivalTimeAtFinanceServicePoint = arrivalTimeAtFinanceServicePoint;
	}

	public double getArrivalTimeAtTestDriveServicePoint() {
		return arrivalTimeAtTestDriveServicePoint;
	}

	public void setArrivalTimeAtTestDriveServicePoint(double arrivalTimeAtTestDriveServicePoint) {
		this.arrivalTimeAtTestDriveServicePoint = arrivalTimeAtTestDriveServicePoint;
	}

	public double getArrivalTimeAtClosureServicePoint() {
		return arrivalTimeAtClosureServicePoint;
	}

	public void setArrivalTimeAtClosureServicePoint(double arrivalTimeAtClosureServicePoint) {
		this.arrivalTimeAtClosureServicePoint = arrivalTimeAtClosureServicePoint;
	}

	public double getRemovalTimeAtArrivalServicePoint() {
		return removalTimeAtArrivalServicePoint;
	}

	public void setRemovalTimeAtArrivalServicePoint(double removalTimeAtArrivalServicePoint) {
		this.removalTimeAtArrivalServicePoint = removalTimeAtArrivalServicePoint;
	}

	public double getRemovalTimeAtFinanceServicePoint() {
		return removalTimeAtFinanceServicePoint;
	}

	public void setRemovalTimeAtFinanceServicePoint(double removalTimeAtFinanceServicePoint) {
		this.removalTimeAtFinanceServicePoint = removalTimeAtFinanceServicePoint;
	}

	public double getRemovalTimeAtTestDriveServicePoint() {
		return removalTimeAtTestDriveServicePoint;
	}

	public void setRemovalTimeAtTestDriveServicePoint(double removalTimeAtTestDriveServicePoint) {
		this.removalTimeAtTestDriveServicePoint = removalTimeAtTestDriveServicePoint;
	}

	public double getRemovalTimeAtClosureServicePoint() {
		return removalTimeAtClosureServicePoint;
	}

	public void setRemovalTimeAtClosureServicePoint(double removalTimeAtClosureServicePoint) {
		this.removalTimeAtClosureServicePoint = removalTimeAtClosureServicePoint;
	}

	public double getTotalTimeAtArrivalServicePoint() {
		return totalTimeAtArrivalServicePoint;
	}

	public void updateTotalTimeAtArrivalServicePoint() {
		this.totalTimeAtArrivalServicePoint = removalTimeAtArrivalServicePoint - arrivalTimeAtArrivalServicePoint;
	}

	public double getTotalTimeAtFinanceServicePoint() {
		return totalTimeAtFinanceServicePoint;
	}

	public void updateTotalTimeAtFinanceServicePoint() {
		this.totalTimeAtFinanceServicePoint = removalTimeAtFinanceServicePoint - arrivalTimeAtFinanceServicePoint;
	}

	public double getTotalTimeAtTestDriveServicePoint() {
		return totalTimeAtTestDriveServicePoint;
	}

	public void updateTotalTimeAtTestDriveServicePoint() {
		this.totalTimeAtTestDriveServicePoint = removalTimeAtTestDriveServicePoint - arrivalTimeAtTestDriveServicePoint;
	}

	public double getTotalTimeAtClosureServicePoint() {
		return totalTimeAtClosureServicePoint;
	}

	public void updateTotalTimeAtClosureServicePoint() {
		this.totalTimeAtClosureServicePoint = removalTimeAtClosureServicePoint - arrivalTimeAtClosureServicePoint;
	}



	public double calculateFinanceProbability(double customerCreditScore) {
		if (customerCreditScore >= BASECREDITSCORE) {
			return BASEPROB;
		} else {
			// Sales probability formula:
			// saleProbability = basePrice * e^(-coefficient * (customerPrice - basePrice))
			financeProb = BASEPROB * Math.exp(-COEFFICIENT * (customerCreditScore - BASECREDITSCORE));
			return financeProb;
		}
	}

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
