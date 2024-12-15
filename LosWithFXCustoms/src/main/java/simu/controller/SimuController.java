package simu.controller;


import simu.dao.CarsDao;
import simu.framework.Clock;
import simu.model.*;
import java.util.*;


public class SimuController implements Runnable {
    // Instance variables
    private MyEngine myEngine;
    private Car car;
    private CarsDao carsDao;

    // Means and variances
    private int arrivalMean;
    private final int ARRIVALINTERVALMULTIPLIER = 200;
    private int simulationTime;
    private boolean simulationInitialized = false;
    private String statusMessage;
    private ArrayList<String[]> carsToBeCreated;
    private boolean simulationComplete = false;


    public SimuController() {
        myEngine = new MyEngine();
        carsDao = new CarsDao();
    }

    /**
     * Initializes the simulation with the specified parameters.
     *
     * <p>This method configures the simulation engine, sets up service points, initializes
     * cars to be created, and adjusts simulation settings such as speed, service means,
     * and variances. It prepares the simulation environment to be ready for execution.</p>
     *
     * <p><strong>Key Configuration Steps:</strong></p>
     * <ul>
     *   <li>Resets the simulation state.</li>
     *   <li>Configures means and variances for arrival, finance, test-drive, and closure services.</li>
     *   <li>Sets the simulation speed and initializes the list of cars to be created.</li>
     *   <li>Creates and configures service points for each stage of the simulation process.</li>
     *   <li>Sets up the arrival interval and process.</li>
     * </ul>
     *
     * @param arrivalMean             Mean time between arrivals in time units.
     * @param arrivalVariance         Variance in time between arrivals in time units.
     * @param financeMean             Mean time for the finance service in time units.
     * @param financeVariance         Variance in the finance service time in time units.
     * @param testDriveMean           Mean time for the test-drive service in time units.
     * @param testDriveVariance       Variance in the test-drive service time in time units.
     * @param closureMean             Mean time for the closure service in time units.
     * @param closureVariance         Variance in the closure service time in time units.
     * @param simulationSpeed         Simulation speed multiplier (e.g., 1 for normal speed).
     * @param carsToBeCreated         List of cars to be created during the simulation.
     * @param arrivalServicePoints    Number of service points for arrivals.
     * @param financeServicePoints    Number of service points for finance services.
     * @param testDriveServicePoints  Number of service points for test-drive services.
     * @param closureServicePoints    Number of service points for closure services.
     */
    public void initializeSimulation(int arrivalMean, int arrivalVariance, int financeMean, int financeVariance,
                                     int testDriveMean, int testDriveVariance, int closureMean, int closureVariance,
                                     int simulationSpeed, ArrayList<String[]> carsToBeCreated, int arrivalServicePoints,
                                     int financeServicePoints, int testDriveServicePoints, int closureServicePoints)
    {
        resetSimulation();
        myEngine.setArrivalMean(arrivalMean);
        System.out.println("Arrival Mean set to: " + myEngine.getArrivalMean());


        myEngine.setArrivalVariance(arrivalVariance);
        System.out.println("Arrival Variance set to: " + myEngine.getArrivalVariance());


        myEngine.setFinanceMean(financeMean);
        System.out.println("Finance Mean set to: " + myEngine.getFinanceMean());


        myEngine.setFinanceVariance(financeVariance);
        System.out.println("Finance Variance set to: " + myEngine.getFinanceVariance());


        myEngine.setTestdriveMean(testDriveMean);
        System.out.println("Test-drive Mean set to: " + myEngine.getTestdriveMean());


        myEngine.setTestdriveVariance(testDriveVariance);
        System.out.println("Test-drive Variance set to: " + myEngine.getTestdriveVariance());


        myEngine.setClosureMean(closureMean);
        System.out.println("Closure Mean set to: " + myEngine.getClosureMean());


        myEngine.setClosureVariance(closureVariance);
        System.out.println("Closure Variance set to: " + myEngine.getClosureVariance());


        myEngine.setSimulationSpeed(simulationSpeed);
        System.out.println("Simulation Speed set to: " + myEngine.getSimulationSpeed());


        myEngine.carsToBeCreated(carsToBeCreated);
        System.out.println("Cars to be created: " + myEngine.getCarDealerShop().getCarCollection().size());


        myEngine.setArrivalServicePoints(arrivalServicePoints);
        System.out.println("Arrival Service Points set to: " + myEngine.getArrivalServicePoints());


        myEngine.setFinanceServicePoints(financeServicePoints);
        System.out.println("Finance Service Points set to: " + myEngine.getFinanceServicePoints());


        myEngine.setTestdriveServicePoints(testDriveServicePoints);
        System.out.println("Test-drive Service Points set to: " + myEngine.getTestdriveServicePoints());


        myEngine.setClosureServicePoints(closureServicePoints);
        System.out.println("Closure Service Points set to: " + myEngine.getClosureServicePoints());


        myEngine.setTotalServicePoints(myEngine.getArrivalServicePoints(), myEngine.getFinanceServicePoints(),
                myEngine.getTestdriveServicePoints(), myEngine.getClosureServicePoints());
        System.out.println("Total Service Points: " + myEngine.getTotalServicePoints());


        myEngine.setServicePoints(myEngine.getTotalServicePoints());
        System.out.println("Service Points initialized.");


        myEngine.setArrivalServiceTime(arrivalMean, arrivalVariance);
        System.out.println("Arrival Service Time set with mean: " + arrivalMean + ", variance: " + arrivalVariance);


        myEngine.setFinanceServiceTime(financeMean, financeVariance);
        System.out.println("Finance Service Time set with mean: " + financeMean + ", variance: " + financeVariance);


        myEngine.setTestdriveServiceTime(testDriveMean, testDriveVariance);
        System.out.println("Test-drive Service Time set with mean: " + testDriveMean + ", variance: " + testDriveVariance);


        myEngine.setClosureServiceTime(closureMean, closureVariance);
        System.out.println("Closure Service Time set with mean: " + closureMean + ", variance: " + closureVariance);


        myEngine.createArrivalServicePoints(myEngine.getArrivalServicePoints(), myEngine.getArrivalServiceTime(), myEngine.getEventList());
        System.out.println("Arrival Service Points created.");


        myEngine.createFinanceServicePoints(myEngine.getFinanceServicePoints(), myEngine.getFinanceServiceTime(), myEngine.getEventList());
        System.out.println("Finance Service Points created.");


        myEngine.createTestdriveServicePoints(myEngine.getTestdriveServicePoints(), myEngine.getTestdriveServiceTime(), myEngine.getEventList(), myEngine.getCarDealerShop());
        System.out.println("Test-drive Service Points created.");


        myEngine.createClosureServicePoints(myEngine.getClosureServicePoints(), myEngine.getClosureServiceTime(), myEngine.getEventList(), myEngine.getCarDealerShop());
        System.out.println("Closure Service Points created.");


        myEngine.getCarDealerShop().setMeanCarSalesProbability();
        double salesProb = myEngine.getCarDealerShop().getMeanCarSalesProbability();
        System.out.println("Mean Car Sales Probability set: " + salesProb);


        myEngine.setArrivalInterval(salesProb);
        System.out.println("Arrival Interval set to: " + myEngine.getArrivalInterval());


        myEngine.setArrivalProcess(myEngine.getArrivalInterval(), myEngine.getEventList());
        System.out.println("Arrival Process initialized.");


        System.out.println(Clock.getInstance().getClock());
        simulationInitialized = true;
        statusMessage = "Simulation Initialized.";
        System.out.println("Simulation Initialized.");
    }


    /**
     * Resets the simulation to its initial state.
     *
     * <p>This method clears all configurations, including service points, car collections,
     * simulation speed, event lists, and the simulation clock. It prepares the simulation
     * environment for re-initialization or a fresh start.</p>
     *
     * <p><strong>Key Reset Actions:</strong></p>
     * <ul>
     *   <li>Clears service points and resets their counts to zero.</li>
     *   <li>Resets the car collection and sales data in the car dealership.</li>
     *   <li>Clears the event list and processed customers.</li>
     *   <li>Resets the simulation clock and speed to default values.</li>
     *   <li>Marks the simulation as incomplete and updates the status message.</li>
     * </ul>
     */
    public void resetSimulation() {
        // Reset simulation speed
        myEngine.setSimulationSpeed(1); // Default speed to 1
        System.out.println("Simulation Speed reset to default (1).");

        // Reset cars to be created
        myEngine.carsToBeCreated(new ArrayList<>());
        System.out.println("Cars to be created reset to an empty list.");

        // Reset service points
        myEngine.setArrivalServicePoints(0);
        myEngine.setFinanceServicePoints(0);
        myEngine.setTestdriveServicePoints(0);
        myEngine.setClosureServicePoints(0);
        System.out.println("All service points reset to 0.");

        myEngine.setTotalServicePoints(0, 0, 0, 0);
        myEngine.setServicePoints(0);
        myEngine.setCurrentServicePointIndex(0);
        System.out.println("Total service points reset to 0.");

        // Clear existing service points
        myEngine.setArrivalServicePoints(0);
        myEngine.setFinanceServicePoints(0);
        myEngine.setTestdriveServicePoints(0);
        myEngine.setClosureServicePoints(0);
        System.out.println("All service points cleared.");

        // Reset car dealer shop and sales probability
        myEngine.getCarDealerShop().getCarCollection().clear();
        myEngine.getCarDealerShop().getSoldCars().clear();
        System.out.println("Car dealer shop reset: car collection cleared, sales probability reset.");

        // Reset arrival interval and process
        myEngine.setArrivalInterval(0);
        myEngine.getProcessedCustomer().clear();
        System.out.println("Arrival interval and process reset.");

        // Reset the event list
        System.out.println("Event list cleared.");

        // Reset simulation clock
        Clock.getInstance().setClock(0);
        System.out.println("Clock reset to initial state.");

        // Update status
        simulationComplete = false;
        simulationInitialized = false;
        statusMessage = "Simulation Reset.";
        System.out.println("Simulation Reset.");
    }


    /**
     * Executes the simulation in a separate thread.
     *
     * <p>This method checks if the simulation has been properly initialized and if the
     * simulation time is valid. If both conditions are met, it starts the simulation.</p>
     *
     * <p><strong>Note:</strong> This method is intended to be executed when the controller
     * is run as a separate thread.</p>
     */
    @Override
    public void run() {
        if (!simulationInitialized) {
            System.err.println("Simulation not initialized. Please initialize the simulation before starting it.");
            return;
        }
        if (simulationTime <= 0) {
            System.err.println("Simulation time is not set or invalid. Set a valid simulation time before starting.");
            return;
        }
        startSimulation(simulationTime);
    }

    /**
     * Starts the simulation for a specified duration.
     *
     * <p>This method delegates to the simulation engine to execute the simulation for
     * the given duration. It sets the simulation time, starts the engine, and updates
     * the simulation's completion status upon finishing.</p>
     *
     * @param simulationTime Total duration of the simulation in time units.
     */
    public void startSimulation(int simulationTime) {
        this.simulationTime = simulationTime;
        myEngine.setSimulationTime(simulationTime);
        myEngine.run();
        simulationComplete = true;
    }


    /**
     * @hidden
     */
    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }
    /**
     * @hidden
     */
    public boolean isSimulationComplete() {
        return simulationComplete;
    }
    /**
     * @hidden
     */
    public HashMap<String, Double> getBasePrices() {
        return Car.getBasePrices();
    }
    /**
     * @hidden
     */
    public void creatTable(String tableName) {
        carsDao.createTable(tableName);
    }
    /**
     * @hidden
     */
    public ArrayList<String> getTableNames() {
        return carsDao.getAllTableNames();
    }

    /**
     * Adds a list of cars to a database table for persistence.
     *
     * @param tableName       Name of the table to which cars should be added.
     * @param carsToBeCreated List of cars to be added to the table.
     */
    public void addCarsToTable(String tableName, ArrayList<String[]> carsToBeCreated) {
        carsDao.addCarsToTable(tableName, carsToBeCreated);
    }

    /**
     * Retrieves a list of cars from a specified database table.
     *
     * <p>If the specified table does not exist, an empty list is returned. Each car in
     * the list is represented as a `String[]` containing car attributes (e.g., model,
     * type, price).</p>
     *
     * @param tableName Name of the table from which to retrieve cars.
     * @return List of cars retrieved from the specified table, or an empty list if
     *         the table does not exist.
     */

    public ArrayList<String[]> getCarsToBeCreated(String tableName) {
        ArrayList<String> tableNames = getTableNames();
        ArrayList<String[]> carsToBeCreated = new ArrayList<>();
        if (tableNames.contains(tableName)) {
            carsToBeCreated = carsDao.getCarsToBeAdded(tableName);
        }
        return carsToBeCreated;
    }
    /**
     * @hidden
     */
    public MyEngine getMyEngine() {
        return myEngine;
    }
    /**
     * @hidden
     */
    public Car getCar() {
        return car;
    }
    /**
     * @hidden
     */
    public void setCar(Car car) {
        this.car = car;
    }
}
