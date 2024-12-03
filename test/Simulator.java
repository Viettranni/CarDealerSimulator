package test;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.Car;
import simu.model.MyEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

/* Command-line type User Interface */
public class Simulator {
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
		int arrivalInterval = 200;
		int simulationSpeed = 1000;
		int arrivalServicePoints = 5;
		int financeServicePoints = 5;
		int testdriveServicePoints = 5;
		int closureServicePoints = 5;
		int vanMean = 30000;
		int vanVariance = 5000;
		int suvMean = 40000;
		int suvVariance = 6000;
		int sedanMean = 30000;
		int sedanVariance = 4000;
		int sportMean = 60000;
		int sportVariance = 10000;
		int compactMean = 20000;
		int compactVariance = 3000;
		ArrayList<String[]> carsToBeCreated = new ArrayList<>();

		// GAS

		Car.createCar("1",  suvMean, suvVariance, carsToBeCreated, 10, 1);
		Car.createCar("2",  vanMean, vanVariance,carsToBeCreated, 10, 1);
		Car.createCar("3", sedanMean, sedanVariance, carsToBeCreated, 10, 1);
		Car.createCar("4", sportMean, sportVariance,carsToBeCreated, 10, 1);
		Car.createCar("5", compactMean, compactVariance, carsToBeCreated, 10, 1);


		// HYBRID
		Car.createCar("1",  suvMean, suvVariance, carsToBeCreated, 10, 2);
		Car.createCar("2",  vanMean, vanVariance,carsToBeCreated, 10, 2);
		Car.createCar("3", sedanMean, sedanVariance, carsToBeCreated, 10, 2);
		Car.createCar("4", sportMean, sportVariance,carsToBeCreated, 10, 2);
		Car.createCar("5", compactMean, compactVariance, carsToBeCreated, 10, 2);


		// Electric
		Car.createCar("1",  suvMean, suvVariance, carsToBeCreated, 10, 3);
		Car.createCar("2",  vanMean, vanVariance,carsToBeCreated, 10, 3);
		Car.createCar("3", sedanMean, sedanVariance, carsToBeCreated, 10, 3);
		Car.createCar("4", sportMean, sportVariance,carsToBeCreated, 10, 3);
		Car.createCar("5", compactMean, compactVariance, carsToBeCreated, 10, 3);



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

		MyEngine m = new MyEngine(arrivalMean, arrivalVariance, financeMean, financeVariance, testdriveMean, testdriveVariance, closureMean, closureVariance, simulationSpeed, carsToBeCreated, arrivalServicePoints, financeServicePoints, testdriveServicePoints, closureServicePoints, 200);


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

		m.setSimulationTime(1440);
		m.run();
		// Example usage of the findDuplicateIds method
		Set<Integer> duplicateIds = MyEngine.findDuplicateIds();

		if (!duplicateIds.isEmpty()) {
			System.out.println("Duplicate IDs detected: " + duplicateIds);
		} else {
			System.out.println("No duplicate IDs found.");
		}

	}
}
