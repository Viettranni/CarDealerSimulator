package simu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        carTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
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
        consoleLog.appendText("Simulation started with the following parameters:\n");
        consoleLog.appendText(String.format("Arrival Mean: %.1f, Variance:Arrival Mean: %.1f, Variance: %.1f\n", arrivalMeanSlider.getValue(), arrivalVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Finance Mean: %.1f, Variance: %.1f\n", financeMeanSlider.getValue(), financeVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Closure Mean: %.1f, Variance: %.1f\n", closureMeanSlider.getValue(), closureVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Test Drive Mean: %.1f, Variance: %.1f\n", testDriveMeanSlider.getValue(), testDriveVarianceSlider.getValue()));
        consoleLog.appendText(String.format("Service Points - Arrival: %d, Finance: %d, Closure: %d\n",
                arrivalServicePoints.getValue(), financeServicePoints.getValue(), closureServicePoints.getValue()));
        consoleLog.appendText(String.format("Simulation Speed: %.1fx\n", simulationSpeedSlider.getValue()));
        consoleLog.appendText("Cars in simulation:\n");
        for (Car car : cars) {
            consoleLog.appendText(String.format("- %s (%s): $%.2f\n", car.getModel(), car.getType(), car.getPrice()));
        }
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