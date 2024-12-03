package simu.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import simu.model.Car;
import simu.model.MyEngine;

import java.util.ArrayList;

public class TestController {

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

    @FXML private Slider simulationSpeedSlider;
    @FXML private Label simulationSpeedLabel;

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

    @FXML private TextArea consoleLog;

    private final MyEngine myEngine;
    private final ArrayList<String[]> carsToBeCreated = new ArrayList<>();

    public TestController() {
        this.myEngine = new MyEngine();
    }

    @FXML
    public void initialize() {
        // Initialize sliders
        setupSliders();

        // Initialize combo box
        setupCarTypes();

        // Initialize table
        setupCarTable();

        // Setup price adjustment logic
        setupPriceAdjustment();
    }

    private void setupSliders() {
        setupSlider(arrivalMeanSlider, arrivalMeanLabel, " minutes");
        setupSlider(arrivalVarianceSlider, arrivalVarianceLabel, "");
        setupSlider(financeMeanSlider, financeMeanLabel, " minutes");
        setupSlider(financeVarianceSlider, financeVarianceLabel, "");
        setupSlider(closureMeanSlider, closureMeanLabel, " minutes");
        setupSlider(closureVarianceSlider, closureVarianceLabel, "");
        setupSlider(simulationSpeedSlider, simulationSpeedLabel, "x");
    }

    private void setupSlider(Slider slider, Label label, String suffix) {
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            label.setText(String.format("%.1f%s", newVal.doubleValue(), suffix));
        });
    }

    private void setupCarTypes() {
        carType.setItems(FXCollections.observableArrayList("Van", "Compact", "Sedan", "SUV", "Sports"));
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

//                        TODO I have no idea what is this - research needed (getModel - error)
//                        carsToBeCreated.removeIf(c -> c[0].equals(car.getModel()));

                        carTable.getItems().remove(car);
                    });
                }
            }
        });
        carTable.setItems(FXCollections.observableArrayList());
    }

    private void setupPriceAdjustment() {
        priceAdjustmentSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!basePrice.getText().isEmpty()) {
                try {
                    double base = Double.parseDouble(basePrice.getText());
                    double adjusted = base * (1 + newVal.doubleValue() / 100);
                    adjustedPriceLabel.setText(String.format("Adjusted Price: $%.2f", adjusted));
                } catch (NumberFormatException e) {
                    adjustedPriceLabel.setText("Invalid price");
                }
            }
        });
    }

    @FXML
    private void handleAddCar() {
        try {
            String model = carModel.getText();
            String type = carType.getValue();
            double price = Double.parseDouble(basePrice.getText()) * (1 + priceAdjustmentSlider.getValue() / 100);

//            TODO I have no idea what is this - research needed - car input error
//            Car car = new Car(model, type, price);
            carsToBeCreated.add(new String[]{model, type, String.valueOf(price)});
//            carTable.getItems().add(car);

            carModel.clear();
            carType.getSelectionModel().clearSelection();
            basePrice.clear();
            priceAdjustmentSlider.setValue(0);
        } catch (NumberFormatException e) {
            consoleLog.appendText("Error: Invalid input for car price.\n");
        }
    }

    @FXML
    private void handleStartSimulation() {
        try {
            int arrivalMean = (int) arrivalMeanSlider.getValue();
            int arrivalVariance = (int) arrivalVarianceSlider.getValue();
            int financeMean = (int) financeMeanSlider.getValue();
            int financeVariance = (int) financeVarianceSlider.getValue();
            int closureMean = (int) closureMeanSlider.getValue();
            int closureVariance = (int) closureVarianceSlider.getValue();
            int simulationSpeed = (int) simulationSpeedSlider.getValue();
            int arrivalPoints = arrivalServicePoints.getValue();
            int financePoints = financeServicePoints.getValue();
            int closurePoints = closureServicePoints.getValue();


//            TODO error because we don't have such a function in ourEngine
//            myEngine.initializeSimulation(
//                    arrivalMean, arrivalVariance,
//                    financeMean, financeVariance,
//                    closureMean, closureVariance,
//                    simulationSpeed, carsToBeCreated,
//                    arrivalPoints, financePoints, 0, closurePoints, 1440
//            );

            myEngine.run();

            consoleLog.appendText("Simulation started successfully!\n");
        } catch (Exception e) {
            consoleLog.appendText("Error: Could not start simulation.\n");
        }
    }
}
