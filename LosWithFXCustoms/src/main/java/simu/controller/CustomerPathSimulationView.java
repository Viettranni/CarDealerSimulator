package simu.controller;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CustomerPathSimulationView extends Pane {

    private ImageView backgroundView;
    private ImageView customerView;
    private ImageView carView;
    private ImageView roofView;

    private Image defaultCustomerGif;
    private Image runningLeftGif;
    private Image runningRightGif;

    public CustomerPathSimulationView() {
        initialize();
    }

    private void initialize() {
        // Set up the background
        Image backgroundImage = new Image("background2.png");
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(true);
        backgroundView.setFitWidth(705);
        backgroundView.setFitHeight(673);

        // Set up the customer
        defaultCustomerGif = new Image("customer2.gif");
        runningLeftGif = new Image("customerleft.gif");
        runningRightGif = new Image("customerright.gif");

        customerView = new ImageView(defaultCustomerGif);
        customerView.setFitWidth(150);
        customerView.setFitHeight(100);
        customerView.setX((705 - customerView.getFitWidth()) / 2);
        customerView.setY(673 - customerView.getFitHeight() - 10);

        // Set up the car
        Image carImage = new Image("car.png");
        carView = new ImageView(carImage);
        carView.setFitWidth(50);
        carView.setFitHeight(100);
        carView.setX(705 - 415);
        carView.setY(250);

        // Set up the roof
        Image roofImage = new Image("roof.png");
        roofView = new ImageView(roofImage);
        roofView.setFitWidth(150);
        roofView.setFitHeight(130);
        roofView.setX(705 - 460);
        roofView.setY(25);

        getChildren().addAll(backgroundView, customerView, carView, roofView);
    }

    public void startAnimation() {
        SequentialTransition sequence = createAnimationSequence();
        sequence.play();
    }

    private SequentialTransition createAnimationSequence() {
        // Create all the TranslateTransition objects as in the original code
        TranslateTransition moveUpToSP1 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP1.setByY(-100);
        moveUpToSP1.setOnFinished(event -> {
            customerView.setImage(runningLeftGif);
            customerView.setFitWidth(120);
            customerView.setFitHeight(80);
        });

        // ... (create all other TranslateTransition objects)
        TranslateTransition moveLeftToSP1 = new TranslateTransition(Duration.seconds(1), customerView);
        moveLeftToSP1.setByX(-60);
        moveLeftToSP1.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif); // Set left-running GIF
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveUpToSP2 = new TranslateTransition(Duration.seconds(1), customerView);
        moveUpToSP2.setByY(-20);
        moveUpToSP2.setOnFinished(event -> {
            customerView.setImage(runningRightGif); // Reset to default GIF
            customerView.setFitWidth(120);
            customerView.setFitHeight(80);
        });

        TranslateTransition moveRightToSP2 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP2.setByX(170);
        moveRightToSP2.setOnFinished(event -> {
            customerView.setImage(runningRightGif); // Set right-running GIF

        });

        TranslateTransition moveRightToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP3.setByX(100);
        moveRightToSP3.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif); // Set right-running GIF
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveUpToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP3.setByY(-270);
        moveUpToSP3.setOnFinished(event -> {
            customerView.setImage(runningLeftGif); // Reset to default GIF
            customerView.setFitWidth(120);
            customerView.setFitHeight(80);
        });

        TranslateTransition moveLeftToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveLeftToSP3.setByX(-40);
        moveLeftToSP3.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif); // Set left-running GIF
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveDownToSP4 = new TranslateTransition(Duration.seconds(2), customerView);
        moveDownToSP4.setByY(150);
        moveDownToSP4.setOnFinished(event -> {
            customerView.setImage(runningLeftGif); // Reset to default GIF
            customerView.setFitWidth(120);
            customerView.setFitHeight(80);
        });

        TranslateTransition moveLeftToSP4 = new TranslateTransition(Duration.seconds(2), customerView);
        moveLeftToSP4.setByX(-120);
        moveLeftToSP4.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif);
            TranslateTransition moveCarUp = new TranslateTransition(Duration.seconds(2), carView);
            moveCarUp.setByY(-220);
            customerView.setVisible(false);
            moveCarUp.play();
        });

        return new SequentialTransition(
                moveUpToSP1, moveLeftToSP1,
                moveUpToSP2, moveRightToSP2,
                moveRightToSP3, moveUpToSP3, moveLeftToSP3,
                moveDownToSP4, moveLeftToSP4
        );
    }
}