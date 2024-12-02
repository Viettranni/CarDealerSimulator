package test;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.MyEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/* Command-line type User Interface */
public class Simulator {

	// AUTOMATIC CREATION USING FOR LOOP
	public static void createVan(ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel) {
		String carType = "2";
		String amountOfCars = String.valueOf(numberOfCars);
		String fuelType = String.valueOf(typeOfFuel);
		String carMean = "30000";  // Example mean price
		String carVariance = "5000";  // Example price variance
		// Add each car to the list
		carsToBeCreated.add(new String[]{"2", amountOfCars, fuelType, carMean, carVariance});

	}

	public static void createSUV(ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel) {
		String carType = "1";
		String amountOfCars = String.valueOf(numberOfCars);
		String fuelType = String.valueOf(typeOfFuel);
		String carMean = "35000";  // Example mean price
		String carVariance = "6000";  // Example price variance

		// Add each car to the list
		carsToBeCreated.add(new String[]{carType, amountOfCars, fuelType, carMean, carVariance});

	}

	public static void createSedan(ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel) {
		String carType = "3";
		String amountOfCars = String.valueOf(numberOfCars);
		String fuelType = String.valueOf(typeOfFuel);
		String carMean = "25000";  // Example mean price
		String carVariance = "4000";  // Example price variance

		// Add each car to the list
		carsToBeCreated.add(new String[]{carType, amountOfCars, fuelType, carMean, carVariance});

	}

	public static void createSport(ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel) {
		String carType = "4";
		String amountOfCars = String.valueOf(numberOfCars);
		String fuelType = String.valueOf(typeOfFuel);
		String carMean = "50000";  // Example mean price
		String carVariance = "10000";  // Example price variance

		// Add each car to the list
		carsToBeCreated.add(new String[]{carType, amountOfCars, fuelType, carMean, carVariance});

	}

	public static void createCompact(ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel) {
		String carType = "5";
		String amountOfCars = String.valueOf(numberOfCars);
		String fuelType = String.valueOf(typeOfFuel);
		String carMean = "18000";  // Example mean price
		String carVariance = "3000";  // Example price variance

		// Add each car to the list
		carsToBeCreated.add(new String[]{carType, amountOfCars, fuelType, carMean, carVariance});

	}


	// USER INPUT USING SCANNER
	public static void createVan(ArrayList<String[]> carsToBeCreated){
		boolean valid = true;
		Scanner scanner = new Scanner(System.in);
		String amountOfCarsToBeCreated = null;
		String fuelType = null;
		String carMean = null;
		String carVariance = null;
		while (valid) {
			try {
				System.out.println("How many Vans you want to create? ");
				amountOfCarsToBeCreated = scanner.nextLine();
				System.out.println("Gas (1), Hybrid (2), Electric (3): ");
				fuelType = scanner.nextLine();
				System.out.println("Average Van price: ");
				carMean = scanner.nextLine();
				System.out.println("Price variance of the Vans: ");
				carVariance = scanner.nextLine();
				carsToBeCreated.add(new String[]{"2", amountOfCarsToBeCreated, fuelType, carMean, carVariance});
				valid = false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createSUV(ArrayList<String[]> carsToBeCreated){
		boolean valid = true;
		Scanner scanner = new Scanner(System.in);
		String amountOfCarsToBeCreated = null;
		String fuelType = null;
		String carMean = null;
		String carVariance = null;
		while (valid) {
			try {
				System.out.println("How many SUV you want to create? ");
				amountOfCarsToBeCreated = scanner.nextLine();
				System.out.println("Gas (1), Hybrid (2), Electric (3): ");
				fuelType = scanner.nextLine();
				System.out.println("Average SUV price: ");
				carMean = scanner.nextLine();
				System.out.println("Price variance of the SUV: ");
				carVariance = scanner.nextLine();
				carsToBeCreated.add(new String[]{"1", amountOfCarsToBeCreated, fuelType, carMean, carVariance});
				valid = false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createSedan(ArrayList<String[]> carsToBeCreated){
		boolean valid = true;
		Scanner scanner = new Scanner(System.in);
		String amountOfCarsToBeCreated = null;
		String fuelType = null;
		String carMean = null;
		String carVariance = null;
		while (valid) {
			try {
				System.out.println("How many Sedans you want to create? ");
				amountOfCarsToBeCreated = scanner.nextLine();
				System.out.println("Gas (1), Hybrid (2), Electric (3): ");
				fuelType = scanner.nextLine();
				System.out.println("Average Sedan price: ");
				carMean = scanner.nextLine();
				System.out.println("Price variance of the Sedans: ");
				carVariance = scanner.nextLine();
				carsToBeCreated.add(new String[]{"3", amountOfCarsToBeCreated, fuelType, carMean, carVariance});
				valid = false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createSport(ArrayList<String[]> carsToBeCreated){
		boolean valid = true;
		Scanner scanner = new Scanner(System.in);
		String amountOfCarsToBeCreated = null;
		String fuelType = null;
		String carMean = null;
		String carVariance = null;
		while (valid) {
			try {
				System.out.println("How many Sport cars you want to create? ");
				amountOfCarsToBeCreated = scanner.nextLine();
				System.out.println("Gas (1), Hybrid (2), Electric (3): ");
				fuelType = scanner.nextLine();
				System.out.println("Average Sport car price: ");
				carMean = scanner.nextLine();
				System.out.println("Price variance of the Sport cars: ");
				carVariance = scanner.nextLine();
				carsToBeCreated.add(new String[]{"4", amountOfCarsToBeCreated, fuelType, carMean, carVariance});
				valid = false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createCompact(ArrayList<String[]> carsToBeCreated){
		boolean valid = true;
		Scanner scanner = new Scanner(System.in);
		String amountOfCarsToBeCreated = null;
		String fuelType = null;
		String carMean = null;
		String carVariance = null;
		while (valid) {
			try {
				System.out.println("How many Compact you want to create? ");
				amountOfCarsToBeCreated = scanner.nextLine();
				System.out.println("Gas (1), Hybrid (2), Electric (3): ");
				fuelType = scanner.nextLine();
				System.out.println("Average Compact price: ");
				carMean = scanner.nextLine();
				System.out.println("Price variance of the Compact: ");
				carVariance = scanner.nextLine();
				carsToBeCreated.add(new String[]{"5", amountOfCarsToBeCreated, fuelType, carMean, carVariance});
				valid = false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		Trace.setTraceLevel(Level.INFO);
		/*
			The code below is only for testing purposes. Will be changed to controller and view class/variables/methods later.
		 */
		Scanner scanner = new Scanner(System.in);
		boolean valid = true;
		int arrivalMean = 0;
		int arrivalVariance = 0;
		int financeMean = 0;
		int financeVariance = 0;
		int testdriveMean = 0;
		int testdriveVariance = 0;
		int closureMean = 0;
		int closureVariance = 0;
		int simulationSpeed = 1000;
		int arrivalServicePoints = 5;
		int financeServicePoints = 5;
		int testdriveServicePoints = 5;
		int closureServicePoints = 5;
		ArrayList<String[]> carsToBeCreated = new ArrayList<>();

		// GAS
		createVan(carsToBeCreated, 10, 1);
		createCompact(carsToBeCreated, 10, 1);
		createSedan(carsToBeCreated, 10, 1);
		createSUV(carsToBeCreated, 10, 1);
		createSport(carsToBeCreated, 10, 1);

		// HYBRID
		createVan(carsToBeCreated, 10, 2);
		createCompact(carsToBeCreated, 10, 2);
		createSedan(carsToBeCreated, 10, 2);
		createSUV(carsToBeCreated, 10, 2);
		createSport(carsToBeCreated, 10, 2);

		// Electric

		createVan(carsToBeCreated, 10, 3);
		createCompact(carsToBeCreated, 10, 3);
		createSedan(carsToBeCreated, 10, 3);
		createSUV(carsToBeCreated, 10, 3);
		createSport(carsToBeCreated, 10, 3);



		while (valid) {
			try {
				System.out.println("Enter the average arrival service time: ");
				arrivalMean = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the variance of arrival service time: ");
				arrivalVariance = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the average financing service time: ");
				financeMean = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the variance of financing service time: ");
				financeVariance = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the average test-drive service time: ");
				testdriveMean = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the variance of test-drive service time: ");
				testdriveVariance = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the average closure service time: ");
				closureMean = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter the variance of closure service time: ");
				closureVariance = Integer.parseInt(scanner.nextLine());
				valid = false;
			} catch (NumberFormatException e) {
				System.out.println("Enter a valid number");
			}
		}
		System.out.println("Gas cars:");
		System.out.println("Vans: " + Arrays.toString(carsToBeCreated.get(0)));
		System.out.println("Compacts: " + Arrays.toString(carsToBeCreated.get(1)));
		System.out.println("Sedans: " + Arrays.toString(carsToBeCreated.get(2)));
		System.out.println("SUVs: " + Arrays.toString(carsToBeCreated.get(3)));
		System.out.println("Sports: " + Arrays.toString(carsToBeCreated.get(4)));

		System.out.println("Hybrid cars:");
		System.out.println("Vans: " + Arrays.toString(carsToBeCreated.get(5)));
		System.out.println("Compacts: " + Arrays.toString(carsToBeCreated.get(6)));
		System.out.println("Sedans: " + Arrays.toString(carsToBeCreated.get(7)));
		System.out.println("SUVs: " + Arrays.toString(carsToBeCreated.get(8)));
		System.out.println("Sports: " + Arrays.toString(carsToBeCreated.get(9)));

		System.out.println("Electric cars:");
		System.out.println("Vans: " + Arrays.toString(carsToBeCreated.get(10)));
		System.out.println("Compacts: " + Arrays.toString(carsToBeCreated.get(11)));
		System.out.println("Sedans: " + Arrays.toString(carsToBeCreated.get(12)));
		System.out.println("SUVs: " + Arrays.toString(carsToBeCreated.get(13)));
		System.out.println("Sports: " + Arrays.toString(carsToBeCreated.get(14)));
		MyEngine m = new MyEngine(arrivalMean, arrivalVariance, financeMean, financeVariance, testdriveMean, testdriveVariance, closureMean, closureVariance, simulationSpeed, carsToBeCreated, arrivalServicePoints, financeServicePoints, testdriveServicePoints, closureServicePoints, 10);

		Thread inputThread = new Thread(() -> {
			try {
				while (true) {
					int key = System.in.read(); // Capture user input
					switch (key) {
						case 'w': // Increase speed
							m.speedUpSimulationSpeed(100); // Reduce sleep time (faster simulation)
							Trace.out(Trace.Level.INFO, "Speed increased to: " + m.getSimulationSpeed());
							break;
						case 's': // Decrease speed
							m.slowDownSimulationSpeed(100); // Increase sleep time (slower simulation)
							Trace.out(Trace.Level.INFO, "Speed decreased to: " + m.getSimulationSpeed());
							break;
						case 'q': // Stop the program
							Trace.out(Trace.Level.INFO, "Stopping the simulation...");
							System.exit(0); // Exit the program
							break;
						default:
							// Optionally handle other keys here if needed
							break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		inputThread.start();

		m.setSimulationTime(1000);
		m.run();
	}
}
