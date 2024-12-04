package simu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import simu.framework.Trace;
import javafx.application.Platform;
import simu.model.Customer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class SaedTestController {

    @FXML private Slider arrivalMeanSlider;
    @FXML private Label arrivalMeanLabel;
    @FXML private Slider arrivalVarianceSlider;
    @FXML private Label arrivalVarianceLabel;
    @FXML private Slider financeMeanSlider;
    @FXML private Label financeMeanLabel;
    @FXML private Slider financeVarianceSlider;
    @FXML private Label financeVarianceLabel;
    @FXML private Slider closureMeanSlider;
    @FXML private Label closureMeanLabel;
    @FXML private Slider closureVarianceSlider;
    @FXML private Label closureVarianceLabel;
    @FXML private Slider testDriveMeanSlider;
    @FXML private Label testDriveMeanLabel;
    @FXML private Slider testDriveVarianceSlider;
    @FXML private Label testDriveVarianceLabel;
    @FXML private Spinner<Integer> arrivalServicePoints;
    @FXML private Spinner<Integer> financeServicePoints;
    @FXML private Spinner<Integer> closureServicePoints;
    @FXML private TextField carModel;
    @FXML private ComboBox<String> carType;
    @FXML private TextField basePrice;
    @FXML private Slider priceAdjustmentSlider;
    @FXML private Label adjustedPriceLabel;
    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, String> carModelColumn;
    @FXML private TableColumn<Car, String> carTypeColumn;
    @FXML private TableColumn<Car, Double> carPriceColumn;
    @FXML private TableColumn<Car, Button> carActionColumn;
    @FXML private Slider simulationSpeedSlider;
    @FXML private Label simulationSpeedLabel;
    @FXML private TextArea consoleLog;
    private SimuController simuController;
    private Thread simulationThread;
    ArrayList<String[]> carsToBeCreated;

    private ObservableList<Car> cars = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSliders();
        setupCarTypes();
        setupCarTable();
        setupPriceAdjustment();
    }

    private void setupSliders() {
        setupSlider(arrivalMeanSlider, arrivalMeanLabel, " minutes");
        setupSlider(arrivalVarianceSlider, arrivalVarianceLabel, "");
        setupSlider(financeMeanSlider, financeMeanLabel, " minutes");
        setupSlider(financeVarianceSlider, financeVarianceLabel, "");
        setupSlider(closureMeanSlider, closureMeanLabel, " minutes");
        setupSlider(closureVarianceSlider, closureVarianceLabel, "");
        setupSlider(testDriveMeanSlider, testDriveMeanLabel, " minutes");
        setupSlider(testDriveVarianceSlider, testDriveVarianceLabel, "");
        setupSlider(simulationSpeedSlider, simulationSpeedLabel, "x");
    }

    private void setupSlider(Slider slider, Label label, String suffix) {
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                label.setText(String.format("%.1f%s", newVal.doubleValue(), suffix)));
    }

    private void setupCarTypes() {
        carType.getItems().addAll("Van", "Compact", "Sedan", "SUV", "Sports");
    }

    private void setupCarTable() {
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("fuel type"));
        carTypeColumn.setCellValueFactory(new PropertyValueFactory<>("car type"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        carActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Car car = getTableView().getItems().get(getIndex());
                        cars.remove(car);
                    });
                }
            }
        });
        carTable.setItems(cars);
    }

    private void setupPriceAdjustment() {
        priceAdjustmentSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double basePrice = Double.parseDouble(this.basePrice.getText());
            double adjustedPrice = basePrice * (1 + newVal.doubleValue() / 100);
            adjustedPriceLabel.setText(String.format("Adjusted Price: $%.2f", adjustedPrice));
        });
    }

    @FXML
    private void handleAddCar() {
        String model = carModel.getText();
        String type = carType.getValue();
        double price = Double.parseDouble(basePrice.getText()) * (1 + priceAdjustmentSlider.getValue() / 100);
        cars.add(new Car(model, type, price));
        carModel.clear();
        carType.getSelectionModel().clearSelection();
        basePrice.clear();
        priceAdjustmentSlider.setValue(0);
    }

    @FXML
    private void handleStartSimulation() {
        // Implement simulation logic here
        Trace.setTraceLevel(Trace.Level.INFO);
        simuController = new SimuController();

        int arrivalMean = (int)arrivalMeanSlider.getValue();
        int arrivalVariance = (int) arrivalVarianceSlider.getValue();
        int financeMean = (int) financeMeanSlider.getValue();
        int financeVariance = (int) financeVarianceSlider.getValue();
        int testdriveMean = (int) testDriveMeanSlider.getValue();
        int testdriveVariance = (int) testDriveVarianceSlider.getValue();
        int closureMean = (int) closureMeanSlider.getValue();
        int closureVariance = (int) closureVarianceSlider.getValue();
        int simulationTime = 1440;
        int simulationSpeed = 10;
        int arrivalServicePoint = arrivalServicePoints.getValue();
        int financeServicePoint = financeServicePoints.getValue();
        int testdriveServicePoint = 5;
        int closureServicePoint = closureServicePoints.getValue();
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
        carsToBeCreated = new ArrayList<>();
        consoleLog.appendText("Simulation initialized with the following values:");
        consoleLog.appendText("\nArrival mean: " + arrivalMean + "\nArrival Variance: " + arrivalVariance);
        consoleLog.appendText("\nFinance mean: " + financeMean + "\nFinance Variance: " + financeVariance);
        consoleLog.appendText("\nTest drive mean: " + testdriveMean + "\nTest drive Variance: " + testdriveVariance);
        consoleLog.appendText("\nClosure mean: " + closureMean + "\nClosure Variance: " + closureVariance);
        consoleLog.appendText("\nArrival service points: " + arrivalServicePoint);
        consoleLog.appendText("\nFinance service points: " + financeServicePoint);
        consoleLog.appendText("\nTest drive service points: " + testdriveServicePoint);
        consoleLog.appendText("\nClosure service points: " + closureServicePoint + "\n");

        /*// int amount, int carType, int fuelType, double meanPrice, double priceVariance
        for (int i = 1; i < 4; i++) {
            simuController.createCar(1 ,"1", i,  suvMean, suvVariance, carsToBeCreated);
        }
        for (int i = 1; i < 4; i++) {
            simuController.createCar(1 ,"2", i,  suvMean, suvVariance, carsToBeCreated);
        }
        for (int i = 1; i < 4; i++) {
            simuController.createCar(1 ,"3", i,  suvMean, suvVariance, carsToBeCreated);
        }
        for (int i = 0; i < 4; i++) {
            simuController.createCar(1 ,"4", i,  suvMean, suvVariance, carsToBeCreated);
        }

        for (int i = 1; i < 4; i++) {
            simuController.createCar(10 ,"5", i,  suvMean, suvVariance, carsToBeCreated);
        }*/
        simuController.createCar(10 ,"Suv", "gas",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"SUV", "hybrid",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"suv", "electric",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"Van", "gas",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"VAN", "hybrid",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"van", "electric",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"Sedan", "gas",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"SEDAN", "hybrid",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"sedan", "electric",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"Sport", "gas",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"SPORT", "hybrid",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"sport", "electric",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"Compact", "gas",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"COMPACT", "hybrid",  suvMean, suvVariance, carsToBeCreated);
        simuController.createCar(10 ,"compact", "electric",  suvMean, suvVariance, carsToBeCreated);

        simuController.initializeSimulation(arrivalMean, arrivalVariance, financeMean, financeVariance, testdriveMean,
                                            testdriveVariance, closureMean, closureVariance, simulationSpeed
                                            ,carsToBeCreated, arrivalServicePoint, financeServicePoint,
                                            testdriveServicePoint, closureServicePoint);

        simuController.setSimulationTime(simulationTime);
        simulationThread = new Thread(simuController);
        simulationThread.start();
        // Monitor simulation progress in a separate thread
        new Thread(() -> {
            int carsSoldBefore = 0;
            while (!simuController.isSimulationComplete()) {
                try {
                    int carsSold = simuController.getMyEngine().getCarDealerShop().getSoldCars().size();
                    if (carsSoldBefore != carsSold) {
                       carsSoldBefore = carsSold;
                       Platform.runLater(() -> consoleLog.appendText("Cars sold: " + carsSold + "\n"));
                    }
                    Thread.sleep(200); // Throttle updates
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Platform.runLater(() -> consoleLog.appendText("Simulation interrupted.\n"));
                    return; // Exit the loop gracefully
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> consoleLog.appendText("Error: " + e.getMessage() + "\n"));
                    return; // Handle unexpected exceptions
                }
            }
            results();
        }).start();
    }

    public void results() {
        Platform.runLater(() -> {
            // Clear previous logs
            //consoleLog.clear();

            consoleLog.setFont(new Font("Arial", 1)); // Set font to Arial with size 10
            consoleLog.setWrapText(false);

            // Append the simulation end time and customer count
            consoleLog.appendText("\nSimulation ended at " + simuController.getSimulationTime());
            consoleLog.appendText("\nProcessed Customers: " + simuController.getMyEngine().getProcessedCustomer().size() + "\n");

            // Initialize containers for all car and fuel types
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();

            // Collect all unique car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
            }

            // Print header and customer data
            consoleLog.appendText(String.format(
                    "%-10s %-15s %-15s %-15s %-20s %-15s %-10s %-12s %-18s %-22s %-20s\n",
                    "ID", "Arrival Time", "Removal Time", "Total time", "Preferred Car Type", "Fuel Type",
                    "Budget", "Credit Score", "Finance Accepted", "Happy with Test-drive", "Purchased a Car"
            ));

            double totalArrivalTime = 0, totalRemovalTime = 0, totalTime = 0, totalBudget = 0, totalCreditScore = 0;
            int totalCustomers = simuController.getMyEngine().getProcessedCustomer().size();
            int purchasedCount = 0;

            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                totalArrivalTime += customer.getArrivalTime();
                totalRemovalTime += customer.getRemovalTime();
                totalTime += customer.getTotalTime();
                totalBudget += customer.getBudget();
                totalCreditScore += customer.getCreditScore();
                if (customer.isPurchased()) purchasedCount++;

                consoleLog.appendText(String.format(
                        "%-10d %-15.2f %-15.2f %-15.2f %-20s %-15s %-10.2f %-12d %-18b %-22b %-20b\n",
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
            consoleLog.appendText(String.format(
                    "%-10s %-15.2f %-15.2f %-15.2f %-20s %-15s %-10.2f %-12.2f %-18s %-22s %-20.2f\n",
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
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
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
            consoleLog.appendText("\nCar Type Preferences (All Customers):\n");
            for (String carType : allCarTypes) {
                long count = simuController.getMyEngine().getProcessedCustomer().stream()
                        .filter(customer -> customer.getPreferredCarType().equals(carType))
                        .count();
                consoleLog.appendText(carType + ": " + String.format("%.2f", (count / (double) totalCustomers) * 100) + "%\n");
            }

            // Print fuel type preferences
            consoleLog.appendText("\nFuel Type Preferences (All Customers):\n");
            for (String fuelType : allFuelTypes) {
                long count = simuController.getMyEngine().getProcessedCustomer().stream()
                        .filter(customer -> customer.getPreferredFuelType().equals(fuelType))
                        .count();
                consoleLog.appendText(fuelType + ": " + String.format("%.2f", (count / (double) totalCustomers) * 100) + "%\n");
            }

            // Print sold percentages by car type
            consoleLog.appendText("\nCar Type Sold Percentages:\n");
            for (String carType : allCarTypes) {
                int soldCount = carTypeSoldCounts.getOrDefault(carType, 0);
                consoleLog.appendText(carType + ": " + String.format("%.2f", (soldCount / (double) totalSoldCars) * 100) + "%\n");
            }

            // Print sold percentages by fuel type
            consoleLog.appendText("\nFuel Type Sold Percentages:\n");
            for (String fuelType : allFuelTypes) {
                int soldCount = fuelTypeSoldCounts.getOrDefault(fuelType, 0);
                consoleLog.appendText(fuelType + ": " + String.format("%.2f", (soldCount / (double) totalSoldCars) * 100) + "%\n");
            }

            // Print sold percentages by car-fuel combinations
            consoleLog.appendText("\nCar Type and Fuel Type Combination Sold Percentages:\n");
            for (String combination : carFuelTypeSoldCounts.keySet()) {
                int soldCount = carFuelTypeSoldCounts.get(combination);
                consoleLog.appendText(combination + ": " + String.format("%.2f", (soldCount / (double) totalSoldCars) * 100) + "%\n");
            }

            // Print remaining cars and sold cars
            consoleLog.appendText("\nCars left at the dealershop: " + simuController.getMyEngine().getCarDealerShop().getCarCollection().size() + "\n");
            consoleLog.appendText("\nCars sold: " + totalSoldCars + "\n");
        });
    }


    public static class Car {
        private final String model;
        private final String type;
        private final double price;

        public Car(String model, String type, double price) {
            this.model = model;
            this.type = type;
            this.price = price;
        }

        public String getModel() { return model; }
        public String getType() { return type; }
        public double getPrice() { return price; }
    }
}