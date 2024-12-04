package simu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.NumberFormat;
import java.util.Locale;

public class v0_controller {

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
    @FXML private ComboBox<String> engineType;
    @FXML private Label basePriceLabel;
    @FXML private Slider priceAdjustmentSlider;
    @FXML private Label adjustedPriceLabel;
    @FXML private Spinner<Integer> carQuantitySpinner;
    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, String> carTypeColumn;
    @FXML private TableColumn<Car, String> engineTypeColumn;
    @FXML private TableColumn<Car, Double> carPriceColumn;
    @FXML private TableColumn<Car, Integer> carQuantityColumn;
    @FXML private TableColumn<Car, Button> carActionColumn;
    @FXML private Slider simulationSpeedSlider;
    @FXML private Label simulationSpeedLabel;
    @FXML private TextArea consoleLog;

    private ObservableList<Car> cars = FXCollections.observableArrayList();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    @FXML
    public void initialize() {
        setupSliders();
        setupCarTypes();
        setupEngineTypes();
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
        carType.getItems().addAll("Compact", "Van", "SUV", "Sedan", "Sport");
        carType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateBasePrice());
    }

    private void setupEngineTypes() {
        engineType.getItems().addAll("Electric", "Hybrid", "Gasoline");
    }

    private void setupCarTable() {
        carTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        engineTypeColumn.setCellValueFactory(new PropertyValueFactory<>("engineType"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        carQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
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
        priceAdjustmentSlider.setMin(-20);
        priceAdjustmentSlider.setMax(20);
        priceAdjustmentSlider.setValue(0);
        priceAdjustmentSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateAdjustedPrice());
    }

    private void updateBasePrice() {
        String selectedType = carType.getValue();
        double basePrice = getBasePrice(selectedType);
        basePriceLabel.setText("Base Price: " + currencyFormat.format(basePrice));
        updateAdjustedPrice();
    }

    private void updateAdjustedPrice() {
        String selectedType = carType.getValue();
        if (selectedType != null) {
            double basePrice = getBasePrice(selectedType);
            double adjustmentPercentage = priceAdjustmentSlider.getValue() / 100.0;
            double adjustedPrice = basePrice * (1 + adjustmentPercentage);
            adjustedPriceLabel.setText("Adjusted Price: " + currencyFormat.format(adjustedPrice));
        }
    }

    private double getBasePrice(String carType) {
        switch (carType) {
            case "Compact": return 20000;
            case "Van": return 30000;
            case "SUV": return 40000;
            case "Sedan": return 30000;
            case "Sport": return 60000;
            default: return 0;
        }
    }

    @FXML
    private void handleAddCars() {
        String selectedType = carType.getValue();
        String selectedEngineType = engineType.getValue();
        if (selectedType != null && selectedEngineType != null) {
            double basePrice = getBasePrice(selectedType);
            double adjustmentPercentage = priceAdjustmentSlider.getValue() / 100.0;
            double adjustedPrice = basePrice * (1 + adjustmentPercentage);
            int quantity = carQuantitySpinner.getValue();

            cars.add(new Car(selectedType, selectedEngineType, adjustedPrice, quantity));

            // Reset inputs
            carType.getSelectionModel().clearSelection();
            engineType.getSelectionModel().clearSelection();
            priceAdjustmentSlider.setValue(0);
            carQuantitySpinner.getValueFactory().setValue(1);
            basePriceLabel.setText("Base Price: $0");
            adjustedPriceLabel.setText("Adjusted Price: $0");
        }
    }

    @FXML
    private void handleStartSimulation() {
        consoleLog.clear();
        consoleLog.appendText("Simulation started with the following parameters:\n");
        consoleLog.appendText(String.format("Arrival Mean: %.1f, Variance: %.1f\n", arrivalMeanSlider.getValue(), arrivalVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Finance Mean: %.1f, Variance: %.1f\n", financeMeanSlider.getValue(), financeVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Closure Mean: %.1f, Variance: %.1f\n", closureMeanSlider.getValue(), closureVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Test Drive Mean: %.1f, Variance: %.1f\n", testDriveMeanSlider.getValue(), testDriveVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Service Points - Arrival: %d, Finance: %d, Closure: %d\n",
                arrivalServicePoints.getValue(), financeServicePoints.getValue(), closureServicePoints.getValue()));
        consoleLog.appendText(String.format("Simulation Speed: %.1fx\n", simulationSpeedSlider.getValue()));
        consoleLog.appendText("Cars in simulation:\n");
        for (Car car : cars) {
            consoleLog.appendText(String.format("- %s (%s): %s x%d\n",
                    car.getType(), car.getEngineType(), currencyFormat.format(car.getPrice()), car.getQuantity()));
        }
    }

    public static class Car {
        private final String type;
        private final String engineType;
        private final double price;
        private final int quantity;

        public Car(String type, String engineType, double price, int quantity) {
            this.type = type;
            this.engineType = engineType;
            this.price = price;
            this.quantity = quantity;
        }

        public String getType() { return type; }
        public String getEngineType() { return engineType; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }
}