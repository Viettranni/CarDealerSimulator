package simu.controller;


import java.util.*;
import java.io.File;
import javafx.fxml.FXML;
import java.io.FileWriter;
import simu.model.Customer;
import java.io.IOException;
import simu.framework.Clock;
import simu.framework.Trace;
import java.io.BufferedWriter;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;
import simu.view.ShrekPathView;


public class MainController {
    public TextField dealerShipName;
    @FXML private TextField meanPrice;
    @FXML private TextField priceVariance;
    @FXML private TextField amountOfCars;
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
    @FXML private ComboBox<String> carType;
    @FXML private ComboBox<String> fuelType;
    @FXML private ComboBox<String> carSets;
    @FXML private TextField basePrice;
    @FXML private TableView<String[]> carTable;
    @FXML private TableColumn<String[], String> carAmountColumn;
    @FXML private TableColumn<String[], String> carTypeColumn;
    @FXML private TableColumn<String[], String> carFuelTypeColumn;
    @FXML private TableColumn<String[], String> carMeanPriceColumn;
    @FXML private TableColumn<String[], String> carPriceVarianceColumn;
    @FXML private TableColumn<String[], Button> carActionColumn;
    @FXML private Slider simulationSpeedSlider;
    @FXML private Label simulationSpeedLabel;
    @FXML private TextArea consoleLog;
    @FXML private Label consoleLogLabel;
    @FXML private Slider simulationDurationSlider;
    @FXML private Label simulationDurationLabel;
    // ------------------------------- animation stuff -----------------------------//
    @FXML private TabPane rightSideTabPane;
    @FXML private StackPane animationContainer;
    ShrekPathView view;
    AnimationController controller;
    // ------------------------------- animation stuff -----------------------------//
    private SimuController simuController = new SimuController();
    private Thread simulationThread;
    private ArrayList<String[]> carsToBeCreated = new ArrayList<>();
    private int simulationSpeed;
    private String dataBaseTableName;

    /**
     * Initializes the UI components by setting up sliders, car type listeners, car set listeners,
     * table view listeners, car types, fuel types, car sets, and animation view.
     */
    @FXML
    public void initialize() {
        setupSliders();
        setupCarTypeListener();
        setupCarSetsListener();
        setupTableViewListener();
        setupCarTypes();
        setUpFuelTypes();
        setupCarSets();
        setupCarTable();
        setupAnimationView();
    }

    /**
     * Sets up the animation view by initializing the CustomerPathSimulationView and its controller,
     * and adding the view to the animation container.
     */
    private void setupAnimationView() {
        view = new ShrekPathView();
        controller = new AnimationController(view);
        animationContainer.getChildren().add(view);
    }


    /**
     * Configures the sliders for various parameters and sets their value listeners
     * to update corresponding labels with formatted values.
     *
     * Uses <code>setupSlider</code> as utility method.
     */
    public void setupSliders() {
        setupSlider(arrivalMeanSlider, arrivalMeanLabel, " minutes");
        setupSlider(arrivalVarianceSlider, arrivalVarianceLabel, "");
        setupSlider(financeMeanSlider, financeMeanLabel, " minutes");
        setupSlider(financeVarianceSlider, financeVarianceLabel, "");
        setupSlider(closureMeanSlider, closureMeanLabel, " minutes");
        setupSlider(closureVarianceSlider, closureVarianceLabel, "");
        setupSlider(testDriveMeanSlider, testDriveMeanLabel, " minutes");
        setupSlider(testDriveVarianceSlider, testDriveVarianceLabel, "");
        setupSlider(simulationSpeedSlider, simulationSpeedLabel, "x");
        setupSlider(simulationDurationSlider, simulationDurationLabel, " hours");
    }


    /**
     * Configures a single slider with a value listener to update its associated label
     * with the current slider value and a suffix.
     *
     * @param slider The slider to configure.
     * @param label  The label to update.
     * @param suffix The suffix to append to the slider value.
     */
    public void setupSlider(Slider slider, Label label, String suffix) {
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                label.setText(String.format("%.1f%s", newVal.doubleValue(), suffix)));
    }


    /**
     * Sets up a listener for the car type ComboBox to update the base price field
     * based on the selected car type.
     */
    private void setupCarTypeListener() {
        // Add a listener to carSets (ComboBox)
        carType.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Only update if the value actually changed
            HashMap<String, Double> basePrices = simuController.getBasePrices();
            if (newValue != null && !newValue.equals(oldValue)) {
                basePrice.setText(String.valueOf(basePrices.get(newValue.toLowerCase())));
            }
        });
    }


    /**
     * Sets up a listener for the car sets ComboBox to fetch cars from the database,
     * populate the table, and log the number of car combinations to be created.
     */
    private void setupCarSetsListener() {
        // Add a listener to carSets (ComboBox)
        carSets.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Only update if the value actually changed
            if (newValue != null && !newValue.equals(oldValue)) {
                getCarsFromDb();  // Fetch the new cars based on the selected car type
                populateTable();  // Update the table with the new data
                consoleLog.appendText("Car combinations to be created: " + carsToBeCreated.size() + "\n");
            }
        });
    }


    /**
     * Sets up a listener for the TableView to update the base price field
     * based on the selected row in the table.
     */
    private void setupTableViewListener() {
        // Adding a listener to the selection model
        carTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the index of the selected row
                int selectedIndex = carTable.getSelectionModel().getSelectedIndex();
                String newBasePrice = carsToBeCreated.get(selectedIndex)[5];
                basePrice.setText(newBasePrice);
            }
        });
    }

    private void setupCarTypes() {carType.getItems().addAll("Van", "Compact", "Sedan", "SUV", "Sport");}

    private void setUpFuelTypes() {fuelType.getItems().addAll("Gas", "Hybrid", "Electric");}


    /**
     * Check for value in the CheckBox - reads value and clears it if something found.
     */
    private void setupCarSets() {
        if (simuController == null || simuController.getTableNames() == null) {
            Platform.runLater(() -> consoleLog.appendText("Error: simuController or table names are null."));
            return;
        }
        Platform.runLater(() -> {
        carSets.getItems().clear();
        carSets.getItems().addAll(simuController.getTableNames());
        });
    }


    /**
     * Configures the <code>TableView</code> for displaying car data.
     *
     * <p><STRONG>Key tasks:</STRONG></p>
     * <ul>
     *   <li>Maps table columns to data indices (e.g., car type, fuel type, price).</li>
     *   <li>Adds a styled "Delete" button for row deletion, updating both the <code>TableView</code>
     *   and <code>carsToBeCreated</code> list.</li>
     *   <li>Fetches and populates car data from the database.</li>
     *   <li>Enhances UI with hover effects for the "Delete" button.</li>
     * </ul>
     */
    public void setupCarTable() {
        // Set cell value factories to point to specific columns in the data
        carAmountColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[0])); // Car Amount (index 0)
        carTypeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[1])); // Car Type (index 1)
        carFuelTypeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[2])); // Fuel Type (index 2)
        carMeanPriceColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[3])); // Mean Price (index 3)
        carPriceVarianceColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[4])); // Price variance (index 4)

        // Add delete button to the action column
        carActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                // Configure delete button
                deleteButton.setStyle("-fx-text-fill: #0c0c1e; " +
                        "-fx-background-color: #cc5cb8; " +
                        "-fx-padding: 4 20; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 3px;");

                // Add hover effect
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-text-fill: black; " +
                        "-fx-background-color: #ff77e9; " + // Lighter color on hover
                        "-fx-padding: 4 20; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 3px;"));

                deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-text-fill: #0c0c1e; " +
                        "-fx-background-color: #cc5cb8; " +
                        "-fx-padding: 4 20; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 3px;"));

                deleteButton.setOnAction(event -> {
                    int selectedIndex = getIndex(); // Get current row index
                    if (selectedIndex >= 0) {
                        // Remove row from the table
                        String[] removedRow = carTable.getItems().remove(selectedIndex);

                        // Remove from the underlying ArrayList
                        carsToBeCreated.remove(removedRow);
                        consoleLog.appendText("Car combinations to be created: " + carsToBeCreated.size() + "\n");
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null); // Clear cell content for empty rows
                } else {
                    setGraphic(deleteButton); // Show delete button
                }
            }
        });

        // Load data from the database and populate the table
        getCarsFromDb();  // Call the method to fetch data
        populateTable();  // Populate TableView with fetched data
        consoleLog.appendText("Car combinations to be created: " + carsToBeCreated.size() + "\n");
    }

    private void populateTable() {
        // Convert ArrayList<String[]> to ObservableList<String[]>
        ObservableList<String[]> tableData = FXCollections.observableArrayList(carsToBeCreated);

        // Set the data in the TableView
        carTable.setItems(tableData);
    }


    /**
     * Handles the action of adding a car to the list of cars to be created. Validates input fields,
     * logs the added car data, and updates the table.
     */
    @FXML
    private void handleAddCar() {
        String amount = amountOfCars.getText();
        String type = carType.getValue();
        String carFuelType = fuelType.getValue();
        String carMeanPrice = meanPrice.getText();
        String carPriceVariance = priceVariance.getText();
        String carBasePrice = basePrice.getText();

        if (!amount.isEmpty() && type != null && carFuelType != null && !carMeanPrice.isEmpty() && !carPriceVariance.isEmpty()) {
            if (Integer.parseInt(carMeanPrice) <= 0 && Integer.parseInt(carPriceVariance) <= 0) {
                consoleLog.appendText("Car mean price and price variance cannot be 0 or negative.\n");
                return;
            }
            consoleLog.appendText("Amount: " + amount + "\n" + "CarType: " + type + "\n" + "CarFuelType: " + carFuelType + "\n"
                    + "Car mean price: " + carMeanPrice + "\n" + "Car price variance: " + carPriceVariance + "\n");

            carsToBeCreated.add(new String[]{amount, type.toLowerCase(), carFuelType.toLowerCase(), carMeanPrice, carPriceVariance, carBasePrice});
            populateTable();
            consoleLog.appendText("Car combinations to be created: " + carsToBeCreated.size() + "\n");

            dataBaseTableName = dealerShipName.getText().trim().replace(" ", "_");
        } else {
            consoleLog.appendText("Empty values cannot be added.\n");
        }

        amountOfCars.clear();
        meanPrice.clear();
        priceVariance.clear();
    }


    /**
     * Starts the simulation by initializing parameters, creating cars, saving presets, and starting
     * a new thread for the simulation. Also updates the UI logs to display progress.
     */
    @FXML
    public void handleStartSimulation() {
        // Implement simulation logic here
        Trace.setTraceLevel(Trace.Level.INFO);
        carTable.getItems().clear(); // Clear the table view
        int arrivalMean = (int)arrivalMeanSlider.getValue();
        int arrivalVariance = (int) arrivalVarianceSlider.getValue();
        int financeMean = (int) financeMeanSlider.getValue();
        int financeVariance = (int) financeVarianceSlider.getValue();
        int testDriveMean = (int) testDriveMeanSlider.getValue();
        int testDriveVariance = (int) testDriveVarianceSlider.getValue();
        int closureMean = (int) closureMeanSlider.getValue();
        int closureVariance = (int) closureVarianceSlider.getValue();
        int simulationTime = (int) (simulationDurationSlider.getValue() * 60);
        simulationSpeed = 300;
        int arrivalServicePoint = arrivalServicePoints.getValue();
        int financeServicePoint = financeServicePoints.getValue();
        int testDriveServicePoint = setTestDriveServicePoints();
        int closureServicePoint = closureServicePoints.getValue();
        int amountOfCars = 0;
        if (carsToBeCreated.isEmpty()) {
            Platform.runLater(() -> consoleLog.appendText("Cannot run the simulation without adding car(s)"));
            return;
        }
        consoleLog.clear();
        consoleLog.appendText("Simulation initialized with the following values:");
        consoleLog.appendText("\nArrival mean: " + arrivalMean + "\nArrival Variance: " + arrivalVariance);
        consoleLog.appendText("\nFinance mean: " + financeMean + "\nFinance Variance: " + financeVariance);
        consoleLog.appendText("\nTest drive mean: " + testDriveMean + "\nTest drive Variance: " + testDriveVariance);
        consoleLog.appendText("\nClosure mean: " + closureMean + "\nClosure Variance: " + closureVariance);
        consoleLog.appendText("\nArrival service points: " + arrivalServicePoint);
        consoleLog.appendText("\nFinance service points: " + financeServicePoint);
        consoleLog.appendText("\nTest drive service points: " + testDriveServicePoint);
        consoleLog.appendText("\nClosure service points: " + closureServicePoint);
        for (String[] car : carsToBeCreated) {
            amountOfCars += Integer.parseInt(car[0]);
        }
        consoleLog.appendText("\nAmount of cars to be created: " + amountOfCars + "\n");

        dataBaseTableName = dealerShipName.getText().trim().replace(" ", "_");
        if (!dataBaseTableName.isEmpty()) {
            simuController.creatTable(dataBaseTableName);
            simuController.addCarsToTable(dataBaseTableName, carsToBeCreated);
            consoleLog.appendText("Preset is saved successfully.\n");
        } else {
            consoleLog.appendText("Unfortunately, you didn't provide DealerShip name, preset wouldn't be saved.\n");
        }

        simuController.initializeSimulation(arrivalMean, arrivalVariance, financeMean, financeVariance, testDriveMean,
                                            testDriveVariance, closureMean, closureVariance, simulationSpeed
                                            ,carsToBeCreated, arrivalServicePoint, financeServicePoint,
                                            testDriveServicePoint, closureServicePoint);

        simuController.setSimulationTime(simulationTime);
        simulationThread = new Thread(simuController);
        simulationThread.start();
        setupCarSets();

        // ------------------------------- animation stuff -----------------------------//
        // Monitor simulation progress in a separate thread
        updateUI();

        // Make Shrek running
        new Thread(() -> {
            Platform.runLater(() -> {
                controller.restartAnimation();
                rightSideTabPane.getSelectionModel().select(1); // Switch to Animation tab
            });
        }).start();
        // ------------------------------- animation stuff -----------------------------//
    }


    /**
     * Changes the simulation speed dynamically based on the value of the simulationSpeedSlider.
     * Adjusts the simulation speed in the engine adjusting sleep time value.
     */
    public void changeSimulationSpeed(){
        try {
            int multiplier = (int) simulationSpeedSlider.getValue();
            if (multiplier > 9.9) multiplier = simulationSpeed;
            int newSimulationSpeed = simulationSpeed / multiplier;
            int actualSimulationSpeed = simuController.getMyEngine().getSimulationSpeed();
            if (newSimulationSpeed != actualSimulationSpeed) {
                actualSimulationSpeed = newSimulationSpeed;
                simuController.getMyEngine().setSimulationSpeed(actualSimulationSpeed);
                Platform.runLater(() -> consoleLog.appendText("Simulation speed: " + simuController.getMyEngine().getSimulationSpeed() + "\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> consoleLog.appendText("Error: " + e.getMessage() + "\n"));
            return; // Handle unexpected exceptions
        }
    }

    /**
     * Updates the current simulation time in hours in the UI.
     */
    private void displaySimulationTime(){
        try {
            double newTime = (Clock.getInstance().getClock() / 60);
            Platform.runLater(() -> consoleLogLabel.setText(String.format("Current Simulation time: %.1f hours\n", newTime)));
            Thread.sleep(500);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Calculates the total number of test drive service points based on the number of cars
     * to be created and returns the value.
     *
     * @return The total number of test drive service points.
     */
    public int setTestDriveServicePoints(){
        int testDriveServicePoints = 0;
        for (String[] car : carsToBeCreated) {
            testDriveServicePoints += Integer.parseInt(car[0]);
        }
        return testDriveServicePoints;
    }


    /**
     * Continuously monitors the simulation progress by updating UI components with real-time
     * data, such as simulation time, speed, and customer statistics.
     */
    public void updateUI() {
        new Thread(() -> {
            int customersBefore = 0;
            int carsSoldBefore = 0;
            while (!simuController.isSimulationComplete()) {
                displaySimulationTime();
                changeSimulationSpeed();
                try {
                    int carsSold = simuController.getMyEngine().getCarDealerShop().getSoldCars().size();
                    if (carsSoldBefore != carsSold) {
                        carsSoldBefore = carsSold;
                        Platform.runLater(() -> consoleLog.appendText("Cars sold: " + carsSold + "\n"));
                    }
                    int customers = simuController.getMyEngine().getProcessedCustomer().size();
                    if (customersBefore != customers) {
                        customersBefore = customers;
                        Platform.runLater(() -> consoleLog.appendText("Customers processed: " + customers + "\n"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> consoleLog.appendText("Error: " + e.getMessage() + "\n"));
                    return; // Handle unexpected exceptions
                }
            }
            results();
            String resultsFile = dataBaseTableName != null ? dataBaseTableName : "results";
            saveResultsToCsv(resultsFile);
        }).start();
    }


    private void getCarsFromDb() {
        // Clear previous data in the table
        carsToBeCreated.clear();

        // Get the selected table name from carType ComboBox
        String tableName = carSets.getValue(); // THIS WILL BE CHANGED LATER

        if (tableName != null) {
            // Fetch the new data based on the selected car type
            ArrayList<String[]> cars = simuController.getCarsToBeCreated(tableName);

            // Populate the carsToBeCreated list with the new data
            for (String[] car : cars) {
                String amount = car[0];
                String carType = car[1];
                String fuelType = car[2];
                String meanPrice = car[3];
                String priceVariance = car[4];
                String basePrice = car[5];

                // Add the car data to the list
                carsToBeCreated.add(new String[]{amount, carType, fuelType, meanPrice, priceVariance, basePrice});
            }
        }
    }


    /**
     * Summarizes and displays simulation results in the console log.
     *
     * <p><STRONG>Key tasks:</STRONG></p>
     * <ul>
     *   <li>Stops the animation and updates the UI.</li>
     *   <li>Logs simulation end time, total processed customers, and their data:
     *   arrival/removal times, service durations, car preferences, budget, credit score and purchase status. </li>
     *   <li>Calculates and displays averages for all customers. </li>
     *   <li>Logs statistics for car preferences, sold percentages by type and fuel, and car-fuel combinations. </li>
     *   <li>Lists remaining and sold cars with pricing details. </li>
     * </ul>
     */
    public void results() {
        controller.stopAnimation();
        rightSideTabPane.getSelectionModel().select(0);

        Platform.runLater(() -> {
            // Clear previous logs
            // consoleLog.clear();

            consoleLog.setFont(new Font("Arial", 1)); // Set font to Arial with size 10
            consoleLog.setWrapText(false);

            // Append the simulation end time and customer count
            consoleLogLabel.setText(String.format("Simulation ended at: %.1f hours", (Clock.getInstance().getClock() / 60)));
            consoleLog.appendText("\nSimulation ended in: " + (int) Clock.getInstance().getClock() + " minutes");
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
                    "%-10s %-15s %-15s %-15s %-18s %-18s %-18s %-18s %-20s %-15s %-10s %-12s %-18s %-22s %-20s\n",
                    "ID", "Arrival Time", "Removal Time", "Total Time", "Time At Info", "Time At Finance",
                    "Time At Test Drive", "Time At Closure", "Preferred Car Type", "Fuel Type", "Budget",
                    "Credit Score", "Finance Accepted", "Happy with Test-drive", "Purchased a Car"
            ));

            double totalArrivalTime = 0, totalRemovalTime = 0, totalTime = 0, totalInfoPointTime = 0,
                    totalFinancePointTime = 0, totalTestDrivePointTime = 0, totalClosurePointTime = 0, totalBudget = 0,
                    totalCreditScore = 0;
            int totalCustomers = simuController.getMyEngine().getProcessedCustomer().size();
            int purchasedCount = 0;

            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                totalArrivalTime += customer.getArrivalTime();
                totalRemovalTime += customer.getRemovalTime();
                totalTime += customer.getTotalTime();
                totalInfoPointTime += customer.getTotalTimeAtArrivalServicePoint();
                totalFinancePointTime += customer.getTotalTimeAtFinanceServicePoint();
                totalTestDrivePointTime += customer.getTotalTimeAtTestDriveServicePoint();
                totalClosurePointTime += customer.getTotalTimeAtClosureServicePoint();
                totalBudget += customer.getBudget();
                totalCreditScore += customer.getCreditScore();
                if (customer.isPurchased()) purchasedCount++;

                consoleLog.appendText(String.format(
                        "%-10d %-15.2f %-15.2f %-15.2f %-18.2f %-18.2f %-18.2f %-18.2f %-20s %-15s %-10.2f %-12d %-18b %-22b %-20b\n",
                        customer.getId(),
                        customer.getArrivalTime(),
                        customer.getRemovalTime(),
                        customer.getTotalTime(),
                        customer.getTotalTimeAtArrivalServicePoint(),
                        customer.getTotalTimeAtFinanceServicePoint(),
                        customer.getTotalTimeAtTestDriveServicePoint(),
                        customer.getTotalTimeAtClosureServicePoint(),
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
                    "%-10s %-15.2f %-15.2f %-15.2f %-18.2f %-18.2f %-18.2f %-18.2f %-20s %-15s %-10.2f %-12.2f %-18s %-22s %-20.2f\n",
                    "AVG",
                    totalArrivalTime / totalCustomers,
                    totalRemovalTime / totalCustomers,
                    totalTime / totalCustomers,
                    totalInfoPointTime / totalCustomers,
                    totalFinancePointTime / totalCustomers,
                    totalTestDrivePointTime / totalCustomers,
                    totalClosurePointTime / totalCustomers,
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

            // Print remaining cars with prices
            consoleLog.appendText("\nCars left at the dealer shop:\n");
            for (simu.model.Car car : simuController.getMyEngine().getCarDealerShop().getCarCollection()) {
                consoleLog.appendText(car.getCarType() + " (" + car.getFuelType() + ") - Base price at: $" + car.getBasePrice() + ") - Selling price at: $" + car.getMeanPrice() + "\n");
            }

            for (simu.model.Car car : simuController.getMyEngine().getCarDealerShop().getSoldCars()) {
                if (!simuController.getMyEngine().getCarDealerShop().getSoldCars().isEmpty()) {
                    consoleLog.appendText(car.getCarType() + " (" + car.getFuelType() + ") - Base price at: $" + car.getBasePrice() + ") - Sold at Price: $" + car.getMeanPrice() + "\n");
                }
            }

            // Print remaining cars and sold cars
            consoleLog.appendText("\nCars left at the dealer shop: " + simuController.getMyEngine().getCarDealerShop().getCarCollection().size() + "\n");
            consoleLog.appendText("\nCars sold: " + totalSoldCars + "\n");
        });
    }

    private void saveCustomerBaseDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CustomerBaseData.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write customer data
            writer.write("ID;Arrival Time;Removal Time;Total Time;Time At Info Point;Time At Finance Point;Time At Test Drive point;Time At Closure Point;Preferred Car Type;Fuel Type;Budget;Credit Score;Finance Accepted;Happy with Test-drive; Test Drive Count; Purchased a Car; Seller Price; Base Price\n");
            double totalArrivalTime = 0, totalRemovalTime = 0, totalTime = 0, totalBudget = 0, totalCreditScore = 0;
            int totalCustomers = simuController.getMyEngine().getProcessedCustomer().size();
            int purchasedCount = 0;

            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                totalArrivalTime += customer.getArrivalTime();
                totalRemovalTime += customer.getRemovalTime();
                totalTime += customer.getTotalTime();
                totalBudget += customer.getBudget();
                totalCreditScore += customer.getCreditScore();
                double sellerPrice = customer.getPurchaseCar() != null ? customer.getPurchaseCar().getSellerPrice() : 0;
                double basePrice = customer.getPurchaseCar() != null ? customer.getPurchaseCar().getBasePrice() : 0;
                if (customer.isPurchased()) purchasedCount++;

                writer.write(String.format(
                        "%d;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%s;%s;%.2f;%d;%b;%b; %d; %b; %.2f; %.2f\n",
                        customer.getId(),
                        customer.getArrivalTime(),
                        customer.getRemovalTime(),
                        customer.getTotalTime(),
                        customer.getTotalTimeAtArrivalServicePoint(),
                        customer.getTotalTimeAtFinanceServicePoint(),
                        customer.getTotalTimeAtTestDriveServicePoint(),
                        customer.getArrivalTimeAtClosureServicePoint(),
                        customer.getPreferredCarType(),
                        customer.getPreferredFuelType(),
                        customer.getBudget(),
                        customer.getCreditScore(),
                        customer.isFinanceAccepted(),
                        customer.happyWithTestdrive(),
                        customer.getTestDriveCount(),
                        customer.isPurchased(),
                        sellerPrice,
                        basePrice
                ));
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCustomerCarDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CustomerCarTypeData.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write customer data
            int totalCustomers = simuController.getMyEngine().getProcessedCustomer().size();

            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }

            // Write car type preferences
            writer.write("Car Type;Customer count\n");
            for (String carType : allCarTypes) {
                long count = simuController.getMyEngine().getProcessedCustomer().stream()
                        .filter(customer -> customer.getPreferredCarType().equals(carType))
                        .count();
                writer.write(carType + ";" + String.format("%d", count) + "\n");
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCustomerFuelDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CustomerFuelData.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }
            // Write fuel type preferences
            writer.write("Fuel Type;Customer count\n");
            for (String fuelType : allFuelTypes) {
                long count = simuController.getMyEngine().getProcessedCustomer().stream()
                        .filter(customer -> customer.getPreferredFuelType().equals(fuelType))
                        .count();
                writer.write(fuelType + ";" + String.format("%d", count) + "\n");
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCarsSoldByTypeDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CarsSoldByTypeData.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }

            writer.write("Car Type;Count\n");
            for (String carType : allCarTypes) {
                int soldCount = carTypeSoldCounts.getOrDefault(carType, 0);
                writer.write(carType + ";" + String.format("%d", soldCount)  + "\n");
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCarsSoldByFuelDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CarsSoldByFuelData.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }
            // Write sold cars by fuel type
            writer.write("Fuel Type;Count\n");
            for (String fuelType : allFuelTypes) {
                int soldCount = fuelTypeSoldCounts.getOrDefault(fuelType, 0);
                writer.write(fuelType + ";" + String.format("%d", soldCount) + "\n");
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCarsSoldByTypeAndFuelDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CarsSoldByTypeAndFuel.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }
            // Write sold cars by car-fuel combination
            writer.write("Combination;Count\n");
            for (String combination : carFuelTypeSoldCounts.keySet()) {
                int soldCount = carFuelTypeSoldCounts.get(combination);
                writer.write(combination + ";" + String.format("%d", soldCount) + "\n");
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCarsRemainingDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir +  filePath1 + "CarsRemainingData.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }
            // Write remaining cars
            writer.write("Car Type;Fuel Type;Base Price;Selling Price\n");
            for (simu.model.Car car : simuController.getMyEngine().getCarDealerShop().getCarCollection()) {
                writer.write(car.getCarType() + ";" + car.getFuelType() + ";" + car.getBasePrice() + ";" + car.getMeanPrice() + "\n");
            }
            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCarsSoldDataToCSV(String filePath1) {
        String parentDir = "SimulationResults/";
        File directory = new File(parentDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = parentDir + filePath1 + "CarsSoldData.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Initialize sets and maps for statistics
            Set<String> allCarTypes = new HashSet<>();
            Set<String> allFuelTypes = new HashSet<>();
            Map<String, Integer> carTypeSoldCounts = new HashMap<>();
            Map<String, Integer> fuelTypeSoldCounts = new HashMap<>();
            Map<String, Integer> carFuelTypeSoldCounts = new HashMap<>();
            int totalSoldCars = 0;

            // Collect car and fuel types
            for (Customer customer : simuController.getMyEngine().getProcessedCustomer()) {
                allCarTypes.add(customer.getPreferredCarType());
                allFuelTypes.add(customer.getPreferredFuelType());
                if (customer.isPurchased()) {
                    totalSoldCars++;
                    String carType = customer.getPreferredCarType();
                    String fuelType = customer.getPreferredFuelType();

                    carTypeSoldCounts.put(carType, carTypeSoldCounts.getOrDefault(carType, 0) + 1);
                    fuelTypeSoldCounts.put(fuelType, fuelTypeSoldCounts.getOrDefault(fuelType, 0) + 1);
                    carFuelTypeSoldCounts.put(carType + " - " + fuelType, carFuelTypeSoldCounts.getOrDefault(carType + " - " + fuelType, 0) + 1);
                }
            }
            // Write sold cars
            writer.write("Car Type;Fuel Type;Base Price;Selling Price\n");
            for (simu.model.Car car : simuController.getMyEngine().getCarDealerShop().getSoldCars()) {
                writer.write(car.getCarType() + ";" + car.getFuelType() + ";" + car.getBasePrice() + ";" + car.getMeanPrice() + "\n");
            }

            Platform.runLater(() -> consoleLog.appendText("Results saved successfully to: " + filePath + "\n"));
            System.out.println("Results saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Saves customer data, car preferences, fuel preferences, and simulation results to various
     * CSV files under the SimulationResults directory.
     *
     * @param fileName The base file name for saving results.
     */
    public void saveResultsToCsv(String fileName) {
        saveCustomerBaseDataToCSV(fileName);
        saveCustomerCarDataToCSV(fileName);
        saveCustomerFuelDataToCSV(fileName);
        saveCarsSoldDataToCSV(fileName);
        saveCarsSoldByTypeDataToCSV(fileName);
        saveCarsSoldByFuelDataToCSV(fileName);
        saveCarsSoldByTypeAndFuelDataToCSV(fileName);
        saveCarsRemainingDataToCSV(fileName);
    }

}