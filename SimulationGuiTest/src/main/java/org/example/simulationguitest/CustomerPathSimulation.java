package org.example.simulationguitest;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CustomerPathSimulation extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Scene dimensions
        double sceneWidth = 800;
        double sceneHeight = 600;

        // Pane for the layout
        Pane root = new Pane();

        // Background image
        Image backgroundImage = new Image("background2.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(true);
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);
        backgroundView.setX((sceneWidth - backgroundView.getBoundsInLocal().getWidth()) / 2);
        backgroundView.setY((sceneHeight - backgroundView.getBoundsInLocal().getHeight()) / 2);

        // Customer images (default, left, and right)
        Image defaultCustomerGif = new Image("customer2.gif");
        Image runningLeftGif = new Image("customerleft.gif");
        Image runningRightGif = new Image("customerright.gif");

        // Customer ImageView
        ImageView customerView = new ImageView(defaultCustomerGif);
        customerView.setFitWidth(150);
        customerView.setFitHeight(100);
        customerView.setX((sceneWidth - customerView.getFitWidth()) / 2);
        customerView.setY(sceneHeight - customerView.getFitHeight() - 10);

        // Car ImageView
        Image carImage = new Image("car.png");
        ImageView carView = new ImageView(carImage);
        carView.setFitWidth(50);
        carView.setFitHeight(100);
        carView.setX(sceneWidth - 415);
        carView.setY(250);

        // Roof ImageView
        Image roofImage = new Image("roof.png");
        ImageView roofView = new ImageView(roofImage);
        roofView.setFitWidth(150);
        roofView.setFitHeight(130);
        roofView.setX(sceneWidth - 460);
        roofView.setY(25);

        // Add elements to the scene
        root.getChildren().addAll(backgroundView, customerView, carView, roofView);

        // Create text nodes for service points
        Text sp1Text = new Text("0/1");
        sp1Text.setFont(new Font("Arial", 20));
        sp1Text.setFill(Color.GREEN);
        sp1Text.setX(320);
        sp1Text.setY(sceneHeight - 110);
        root.getChildren().add(sp1Text);

        Text sp2Text = new Text("0/1");
        sp2Text.setFont(new Font("Arial", 20));
        sp2Text.setFill(Color.GREEN);
        sp2Text.setX(sceneWidth - 330);
        sp2Text.setY(sceneHeight - 115);
        root.getChildren().add(sp2Text);

        Text sp3Text = new Text("0/1");
        sp3Text.setFont(new Font("Arial", 20));
        sp3Text.setFill(Color.GREEN);
        sp3Text.setX(sceneWidth - 300);
        sp3Text.setY(170);
        root.getChildren().add(sp3Text);

        Text sp4Text = new Text("0/1");
        sp4Text.setFont(new Font("Arial", 20));
        sp4Text.setFill(Color.GREEN);
        sp4Text.setX(sceneWidth - 335);
        sp4Text.setY(260);
        root.getChildren().add(sp4Text);

        // Movement transitions
        TranslateTransition moveUpToSP1 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP1.setByY(-100);
        moveUpToSP1.setOnFinished(event -> {
            customerView.setImage(runningLeftGif); // Reset to default GIF
            customerView.setFitWidth(120);
            customerView.setFitHeight(80);
        });

        TranslateTransition moveLeftToSP1 = new TranslateTransition(Duration.seconds(1), customerView);
        moveLeftToSP1.setByX(-60);
        moveLeftToSP1.setOnFinished(event -> {
            sp1Text.setText("1/1");
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
            sp1Text.setText("0/1"); // Reset SP1
            sp2Text.setText("1/1");
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
            sp2Text.setText("0/1"); // Reset SP2
            sp3Text.setText("1/1");
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
            customerView.setImage(defaultCustomerGif); // Set left-running GIF
        });

        moveLeftToSP4.setOnFinished(event -> {
            TranslateTransition moveCarUp = new TranslateTransition(Duration.seconds(2), carView);
            moveCarUp.setByY(-220); // Move the car away

            sp3Text.setText("0/1"); // Reset SP3
            sp4Text.setText("1/1");

            customerView.setVisible(false); // Hide the customer
            moveCarUp.play(); // Play car movement
        });

        // Sequence of all movements
        SequentialTransition sequence = new SequentialTransition(
                moveUpToSP1, moveLeftToSP1,
                moveUpToSP2, moveRightToSP2,
                moveRightToSP3, moveUpToSP3, moveLeftToSP3,
                moveDownToSP4, moveLeftToSP4
        );

        // Start the animation sequence
        sequence.play();

        // Set up the scene and stage
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Path Simulation");
        primaryStage.show();
    }
}
