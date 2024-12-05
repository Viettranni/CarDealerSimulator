package simu.controller;


import simu.dao.CarsDao;
import simu.framework.Clock;
import simu.framework.Trace;
import simu.model.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;


public class SimuController implements Runnable {
    // Instance variables
    private MyEngine myEngine;
    private Car car;
    private CarsDao carsDao;


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
    private boolean simulationInitialized = false;
    private String statusMessage;
    private ArrayList<String[]> carsToBeCreated;
    private boolean simulationComplete = false;


    // Parameterless constructor (does not initialize fields)
    public SimuController(MyEngine engine) {
        myEngine = engine;
        carsDao = new CarsDao();
    }


    public SimuController() {
        myEngine = new MyEngine();
        carsDao = new CarsDao();
    }


    public void initializeSimulation(int arrivalMean, int arrivalVariance, int financeMean, int financeVariance,
                                     int testdriveMean, int testdriveVariance, int closureMean, int closureVariance,
                                     int simulationSpeed, ArrayList<String[]> carsToBeCreated, int arrivalServicePoints,
                                     int financeServicePoints, int testdriveServicePoints, int closureServicePoints)
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
        simulationInitialized = true;
        statusMessage = "Simulation Initialized.";
        System.out.println("Simulation Initialized.");
    }

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



    // Thread's run method delegates to startSimulation
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

    public void startSimulation(int simulationTime) {
        this.simulationTime = simulationTime;
        myEngine.setSimulationTime(simulationTime);
        myEngine.run();
        simulationComplete = true;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public boolean isSimulationComplete() {
        return simulationComplete;
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
        simulationComplete = true;
    }


    public void speedUpSimulation(int amount) {
        myEngine.speedUpSimulationSpeed(amount);
    }


    public void slowdownSimulation(int amount) {
        myEngine.slowDownSimulationSpeed(amount);
    }


    public int getSimulationSpeed() {
        return myEngine.getSimulationSpeed();
    }


    // int amount, int carType, int fuelType, double meanPrice, double priceVariance
    public void createCar( int numberOfCars, String carType, String typeOfFuel, int sellerCarMean, int sellerCarVariance, ArrayList<String[]> carsToBeCreated){
        Car.createCar(numberOfCars, carType, typeOfFuel, sellerCarMean, sellerCarVariance, carsToBeCreated);
    }

    public void createCar( int numberOfCars, String carType, String typeOfFuel, int sellerCarMean, int sellerCarVariance, ArrayList<String[]> carsToBeCreated, double basePrice){
        Car.createCar(numberOfCars, carType, typeOfFuel, sellerCarMean, sellerCarVariance, carsToBeCreated, basePrice);
    }

    public ArrayList<String> getTableNames() {
        return carsDao.getAllTableNames();
    }

    public ArrayList<String[]> populateCarsToBeCreated(String tableName) {
        ArrayList<String> tableNames = getTableNames();
        ArrayList<String[]> carsToBeCreated = new ArrayList<>();
        if (tableNames.contains(tableName)) {
            carsToBeCreated = carsDao.populateCarsToBeAdded(tableName);
        }
        return carsToBeCreated;
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
        myEngine.setArrivalMean(arrivalMean);
        this.arrivalMean = myEngine.getArrivalMean();
        System.out.println("Arrival Mean set to: " + myEngine.getArrivalMean());
    }


    public int getArrivalVariance() {
        return arrivalVariance;
    }


    public void setArrivalVariance(int arrivalVariance) {
        myEngine.setArrivalVariance(arrivalVariance);
        System.out.println("Arrival Variance set to: " + myEngine.getArrivalVariance());
    }


    public int getFinanceMean() {
        return financeMean;
    }


    public void setFinanceMean(int financeMean) {
        myEngine.setFinanceMean(financeMean);
        System.out.println("Finance Mean set to: " + myEngine.getFinanceMean());
    }


    public int getFinanceVariance() {
        return financeVariance;
    }


    public void setFinanceVariance(int financeVariance) {
        myEngine.setFinanceVariance(financeVariance);
        System.out.println("Finance Variance set to: " + myEngine.getFinanceVariance());
    }


    public int getTestdriveMean() {
        return testdriveMean;
    }


    public void setTestdriveMean(int testdriveMean) {
        myEngine.setTestdriveMean(testdriveMean);
        System.out.println("Test-drive Mean set to: " + myEngine.getTestdriveMean());
    }


    public int getTestdriveVariance() {
        return testdriveVariance;
    }


    public void setTestdriveVariance(int testdriveVariance) {
        myEngine.setTestdriveVariance(testdriveVariance);
        System.out.println("Test-drive Variance set to: " + myEngine.getTestdriveVariance());
    }


    public int getClosureMean() {
        return closureMean;
    }


    public void setClosureMean(int closureMean) {
        myEngine.setClosureMean(closureMean);
        System.out.println("Closure Mean set to: " + myEngine.getClosureMean());


    }


    public int getClosureVariance() {
        return closureVariance;
    }


    public void setClosureVariance(int closureVariance) {
        myEngine.setClosureVariance(closureVariance);
        System.out.println("Closure Variance set to: " + myEngine.getClosureVariance());
    }


    public void setSimulationSpeed(int simulationSpeed) {
        myEngine.setSimulationSpeed(simulationSpeed);
        System.out.println("Simulation Speed set to: " + myEngine.getSimulationSpeed());
    }


    public int getArrivalServicePoints() {
        return arrivalServicePoints;
    }


    public void setArrivalServicePoints(int arrivalServicePoints) {
        myEngine.setArrivalServicePoints(arrivalServicePoints);
        System.out.println("Arrival Service Points set to: " + myEngine.getArrivalServicePoints());
    }


    public int getFinanceServicePoints() {
        return financeServicePoints;
    }


    public void setFinanceServicePoints(int financeServicePoints) {
        myEngine.setFinanceServicePoints(financeServicePoints);
        System.out.println("Finance Service Points set to: " + myEngine.getFinanceServicePoints());
    }


    public int getTestdriveServicePoints() {
        return testdriveServicePoints;
    }


    public void setTestdriveServicePoints(int testdriveServicePoints) {
        myEngine.setTestdriveServicePoints(testdriveServicePoints);
        System.out.println("Test-drive Service Points set to: " + myEngine.getTestdriveServicePoints());
    }


    public int getClosureServicePoints() {
        return closureServicePoints;
    }


    public void setClosureServicePoints(int closureServicePoints) {
        myEngine.setClosureServicePoints(closureServicePoints);
        System.out.println("Closure Service Points set to: " + myEngine.getClosureServicePoints());
    }


    public void setCarsToBeCreated(ArrayList<String[]> carsToBeCreated) {
        myEngine.carsToBeCreated(carsToBeCreated);
        System.out.println("Cars to be created: " + myEngine.getCarDealerShop().getCarCollection().size());
    }


    public ArrayList<String[]> getCarsToBeCreated() {
        return carsToBeCreated;
    }




    /*public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);
        //MyEngine m = new MyEngine();
        SimuController simuController = new SimuController();
        MyEngine m = simuController.getMyEngine();


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
        // int amount, int carType, int fuelType, double meanPrice, double priceVariance
        for (int i = 1; i < 4; i++) {
            simuController.createCar(10 ,"1", i,  suvMean, suvVariance, carsToBeCreated);
        }
        for (int i = 1; i < 4; i++) {
            simuController.createCar(10 ,"2", i,  suvMean, suvVariance, carsToBeCreated);
        }
        for (int i = 1; i < 4; i++) {
            simuController.createCar(10 ,"3", i,  suvMean, suvVariance, carsToBeCreated);
        }
        for (int i = 0; i < 4; i++) {
            simuController.createCar(10 ,"4", i,  suvMean, suvVariance, carsToBeCreated);
        }

        for (int i = 1; i < 4; i++) {
            simuController.createCar(10 ,"5", i,  suvMean, suvVariance, carsToBeCreated);
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
                            //m.speedUpSimulationSpeed(100); // Reduce sleep time (faster simulation)
                            simuController.speedUpSimulation(100);
                            Trace.out(Trace.Level.INFO, "Speed increased to: " + simuController.getSimulationSpeed());
                            break;
                        case 's': // Decrease speed
                            //m.slowDownSimulationSpeed(100); // Increase sleep time (slower simulation)
                            simuController.slowdownSimulation(100);
                            Trace.out(Trace.Level.INFO, "Speed decreased to: " + simuController.getSimulationSpeed());
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
                arrivalServicePoints, financeServicePoints, testdriveServicePoints, closureServicePoints
        );

        simuController.startSimulation(simulationTime);

        Set<Integer> duplicateIds = MyEngine.findDuplicateIds();

        if (!duplicateIds.isEmpty()) {
            System.out.println("Duplicate IDs detected: " + duplicateIds);
        } else {
            System.out.println("No duplicate IDs found.");
        }
    }*/
}
