package test;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.MyEngine;

import java.io.IOException;
import java.util.Scanner;

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
		int simulationSpeed = 1000;
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
		MyEngine m = new MyEngine(arrivalMean, arrivalVariance, financeMean, financeVariance, testdriveMean, testdriveVariance, closureMean, closureVariance, simulationSpeed);

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
