package simu.controller;

import simu.framework.Clock;
import simu.framework.Trace;
import simu.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class SimuController {
    // Instance variables
    private MyEngine myEngine;
    private Car car;

    // Means and variances
    private int arrivalMean;
    private int arrivalVariance;
    private int financeMean;
    private int financeVariance;
    private int testdriveMean;
    private int testdriveVariance;
    private int closureMean;
    private int closureVariance;
    private int simulationSpeed;
    private int arrivalServicePoints;
    private int financeServicePoints;
    private int testdriveServicePoints;
    private int closureServicePoints;

    private int vanMean;
    private int vanVariance;
    private int suvMean;
    private int suvVariance;
    private int sedanMean;
    private int sedanVariance;
    private int sportMean;
    private int sportVariance;
    private int compactMean;
    private int compactVariance;
    private final int ARRIVALINTERVALMULTIPLIER = 200;
    private int simulationTime;
    private ArrayList<String[]> carsToBeCreated;

    // Parameterless constructor (does not initialize fields)
    public SimuController(MyEngine engine) {
        myEngine = engine;
    }

    public SimuController() {

    }

    public void initializeSimulation(int arrivalMean, int arrivalVariance, int financeMean, int financeVariance,
                                     int testdriveMean, int testdriveVariance, int closureMean, int closureVariance,
                                     int simulationSpeed, ArrayList<String[]> carsToBeCreated, int arrivalServicePoints,
                                     int financeServicePoints, int testdriveServicePoints, int closureServicePoints, int simulationTime)
    {
        myEngine.setArrivalMean(arrivalMean);
        System.out.println("Arrival Mean set to: " + myEngine.getArrivalMean());

        myEngine.setArrivalVariance(arrivalVariance);
        System.out.println("Arrival Variance set to: " + myEngine.getArrivalVariance());

        myEngine.setFinanceMean(financeMean);
        System.out.println("Finance Mean set to: " + myEngine.getFinanceMean());

        myEngine.setFinanceVariance(financeVariance);
        System.out.println("Finance Variance set to: " + myEngine.getFinanceVariance());

        myEngine.setTestdriveMean(testdriveMean);
        System.out.println("Test-drive Mean set to: " + myEngine.getTestdriveMean());

        myEngine.setTestdriveVariance(testdriveVariance);
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

        myEngine.setTestdriveServicePoints(testdriveServicePoints);
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

        myEngine.setTestdriveServiceTime(testdriveMean, testdriveVariance);
        System.out.println("Test-drive Service Time set with mean: " + testdriveMean + ", variance: " + testdriveVariance);

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
    }


    public void startSimulation(int simulationTime) {
        myEngine.setSimulationTime(simulationTime);
        myEngine.run();
    }

    public void createCars(ArrayList<String[]> carsToBeCreated) {
        myEngine.carsToBeCreated(carsToBeCreated);
    }

    public void startSimulation(int arrivalMean, int arrivalVariance, int financeMean, int financeVariance,
                                     int testdriveMean, int testdriveVariance, int closureMean, int closureVariance,
                                     int simulationSpeed, ArrayList<String[]> carsToBeCreated, int arrivalServicePoints,
                                     int financeServicePoints, int testdriveServicePoints, int closureServicePoints, int simulationTime) {

        myEngine = new MyEngine(arrivalMean, arrivalVariance, financeMean, financeVariance, testdriveMean,
                                testdriveVariance, closureMean, closureVariance, simulationSpeed, carsToBeCreated,
                                arrivalServicePoints, financeServicePoints, testdriveServicePoints, closureServicePoints, ARRIVALINTERVALMULTIPLIER
        );
        myEngine.setSimulationTime(simulationTime);
        myEngine.run();
    }

    public void speedUpSimulation(int amount) {
        myEngine.speedUpSimulationSpeed(amount);
    }

    public void slowdownSimulation(int amount) {
        myEngine.slowDownSimulationSpeed(amount);
    }

    public void createCar(String carType, int sellerCarMean, int sellerCarVariance, ArrayList<String[]> carsToBeCreated, int numberOfCars, int typeOfFuel){
        Car.createCar(carType, sellerCarMean, sellerCarVariance, carsToBeCreated, numberOfCars, typeOfFuel);
    }

    // Getters and Setters for all instance variables

    public MyEngine getMyEngine() {
        return myEngine;
    }

    public void setMyEngine(MyEngine myEngine) {
        this.myEngine = myEngine;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    /*public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public CarDealerShop getCarDealerShop() {
        return carDealerShop;
    }

    public void setCarDealerShop(CarDealerShop carDealerShop) {
        this.carDealerShop = carDealerShop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }*/

    public int getArrivalMean() {
        return arrivalMean;
    }

    public void setArrivalMean(int arrivalMean) {
        this.arrivalMean = arrivalMean;
    }

    public int getArrivalVariance() {
        return arrivalVariance;
    }

    public void setArrivalVariance(int arrivalVariance) {
        this.arrivalVariance = arrivalVariance;
    }

    public int getFinanceMean() {
        return financeMean;
    }

    public void setFinanceMean(int financeMean) {
        this.financeMean = financeMean;
    }

    public int getFinanceVariance() {
        return financeVariance;
    }

    public void setFinanceVariance(int financeVariance) {
        this.financeVariance = financeVariance;
    }

    public int getTestdriveMean() {
        return testdriveMean;
    }

    public void setTestdriveMean(int testdriveMean) {
        this.testdriveMean = testdriveMean;
    }

    public int getTestdriveVariance() {
        return testdriveVariance;
    }

    public void setTestdriveVariance(int testdriveVariance) {
        this.testdriveVariance = testdriveVariance;
    }

    public int getClosureMean() {
        return closureMean;
    }

    public void setClosureMean(int closureMean) {
        this.closureMean = closureMean;
    }

    public int getClosureVariance() {
        return closureVariance;
    }

    public void setClosureVariance(int closureVariance) {
        this.closureVariance = closureVariance;
    }

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    public int getArrivalServicePoints() {
        return arrivalServicePoints;
    }

    public void setArrivalServicePoints(int arrivalServicePoints) {
        this.arrivalServicePoints = arrivalServicePoints;
    }

    public int getFinanceServicePoints() {
        return financeServicePoints;
    }

    public void setFinanceServicePoints(int financeServicePoints) {
        this.financeServicePoints = financeServicePoints;
    }

    public int getTestdriveServicePoints() {
        return testdriveServicePoints;
    }

    public void setTestdriveServicePoints(int testdriveServicePoints) {
        this.testdriveServicePoints = testdriveServicePoints;
    }

    public int getClosureServicePoints() {
        return closureServicePoints;
    }

    public void setClosureServicePoints(int closureServicePoints) {
        this.closureServicePoints = closureServicePoints;
    }

    public int getVanMean() {
        return vanMean;
    }

    public void setVanMean(int vanMean) {
        this.vanMean = vanMean;
    }

    public int getVanVariance() {
        return vanVariance;
    }

    public void setVanVariance(int vanVariance) {
        this.vanVariance = vanVariance;
    }

    public int getSuvMean() {
        return suvMean;
    }

    public void setSuvMean(int suvMean) {
        this.suvMean = suvMean;
    }

    public int getSuvVariance() {
        return suvVariance;
    }

    public void setSuvVariance(int suvVariance) {
        this.suvVariance = suvVariance;
    }

    public int getSedanMean() {
        return sedanMean;
    }

    public void setSedanMean(int sedanMean) {
        this.sedanMean = sedanMean;
    }

    public int getSedanVariance() {
        return sedanVariance;
    }

    public void setSedanVariance(int sedanVariance) {
        this.sedanVariance = sedanVariance;
    }

    public int getSportMean() {
        return sportMean;
    }

    public void setSportMean(int sportMean) {
        this.sportMean = sportMean;
    }

    public int getSportVariance() {
        return sportVariance;
    }

    public void setSportVariance(int sportVariance) {
        this.sportVariance = sportVariance;
    }

    public int getCompactMean() {
        return compactMean;
    }

    public void setCompactMean(int compactMean) {
        this.compactMean = compactMean;
    }

    public int getCompactVariance() {
        return compactVariance;
    }

    public void setCompactVariance(int compactVariance) {
        this.compactVariance = compactVariance;
    }

    public ArrayList<String[]> getCarsToBeCreated() {
        return carsToBeCreated;
    }

    public void setCarsToBeCreated(ArrayList<String[]> carsToBeCreated) {
        this.carsToBeCreated = carsToBeCreated;
    }

    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);
        MyEngine m = new MyEngine();
        SimuController simuController = new SimuController(m);

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
        int simulationTime = 1440;
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
        for (int i = 1; i < 4; i++) {
            simuController.createCar("1",  suvMean, suvVariance, carsToBeCreated, 10, i);
        }
        for (int i = 1; i < 4; i++) {
            simuController.createCar("2",  vanMean, vanVariance, carsToBeCreated, 10, i);
        }
        for (int i = 1; i < 4; i++) {
            simuController.createCar("3",  sedanMean, sedanVariance, carsToBeCreated, 10, i);
        }
        for (int i = 0; i < 4; i++) {
            simuController.createCar("4", sportMean, sportVariance, carsToBeCreated, 10, i);
        }

        for (int i = 1; i < 4; i++) {
            simuController.createCar("5",  compactMean, compactVariance, carsToBeCreated, 10, i);
        }

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

        simuController.initializeSimulation( arrivalMean, arrivalVariance, financeMean, financeVariance, testdriveMean,
                                        testdriveVariance, closureMean, closureVariance, simulationSpeed, carsToBeCreated,
                                        arrivalServicePoints, financeServicePoints, testdriveServicePoints, closureServicePoints,
                                        simulationTime
        );


        simuController.startSimulation(simulationTime);

        Set<Integer> duplicateIds = MyEngine.findDuplicateIds();

        if (!duplicateIds.isEmpty()) {
            System.out.println("Duplicate IDs detected: " + duplicateIds);
        } else {
            System.out.println("No duplicate IDs found.");
        }
    }
}
