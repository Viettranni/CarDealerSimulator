package org.example.guitest;

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
        // tää on scene
        double sceneWidth = 800;
        double sceneHeight = 600;

        // Variables for number of customers at each service point
        int sp1_customers = 0, sp2_customers = 0, sp3_customers = 0, sp4_customers = 0;
        int max_customers = 1; // Maximum customers for now

        // pane for the layout
        Pane root = new Pane();

        // background image
        Image backgroundImage = new Image("background2.png");
        ImageView backgroundView = new ImageView(backgroundImage);

        // scale the background to fit within the scene
        backgroundView.setPreserveRatio(true);
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);

        // center the background if there are extra margins
        backgroundView.setX((sceneWidth - backgroundView.getBoundsInLocal().getWidth()) / 2);
        backgroundView.setY((sceneHeight - backgroundView.getBoundsInLocal().getHeight()) / 2);

        // Load the customer image
        Image customerImage = new Image("customer2.gif");
        ImageView customerView = new ImageView(customerImage);
        customerView.setFitWidth(150);
        customerView.setFitHeight(100);

        // set customer at the bottom middle of the scene
        customerView.setX((sceneWidth - customerView.getFitWidth()) / 2);
        customerView.setY(sceneHeight - customerView.getFitHeight() - 10);

        // add images to the pane
        root.getChildren().addAll(backgroundView, customerView);

        // load the car image
        Image carImage = new Image("car.png");
        ImageView carView = new ImageView(carImage);
        carView.setFitWidth(50);
        carView.setFitHeight(100);
        carView.setX(sceneWidth - 415);
        carView.setY(250);
        root.getChildren().add(carView);


        // Create `Text` nodes for service points
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

        // Load the roof image (tää on sitä varten ku auto ajaa katon alle)
        Image roofImage = new Image("roof.png");
        ImageView roofView = new ImageView(roofImage);
        roofView.setFitWidth(150);
        roofView.setFitHeight(130);
        roofView.setX(sceneWidth - 460);
        roofView.setY(25);
        root.getChildren().add(roofView);


        // movements
        // Step 1: move from start to Service Point 1
        TranslateTransition moveUpToSP1 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP1.setByY(-100);

        TranslateTransition moveLeftToSP1 = new TranslateTransition(Duration.seconds(1), customerView);
        moveLeftToSP1.setByX(-60);

        // Step 2: move from Service Point 1 to Service Point 2
        TranslateTransition moveUpToSP2 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP2.setByY(-20);

        TranslateTransition moveRightToSP2 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP2.setByX(170);

        // Step 3: move from Service Point 2 to Service Point 3
        TranslateTransition moveRightToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP3.setByX(100);

        TranslateTransition moveUpToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP3.setByY(-270);

        TranslateTransition moveLeftToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveLeftToSP3.setByX(-40);

        // Step 4: move from Service Point 3 to Service Point 4
        TranslateTransition moveDownToSP4 = new TranslateTransition(Duration.seconds(2), customerView);
        moveDownToSP4.setByY(150);

        TranslateTransition moveLeftToSP4 = new TranslateTransition(Duration.seconds(2), customerView);
        moveLeftToSP4.setByX(-120);

        // Define all sequential movements
        SequentialTransition sequence = new SequentialTransition(
                moveUpToSP1, moveLeftToSP1,
                moveUpToSP2, moveRightToSP2,
                moveRightToSP3, moveUpToSP3, moveLeftToSP3,
                moveDownToSP4, moveLeftToSP4
        );

        // Update text dynamically as customer reaches each service point
        moveLeftToSP1.setOnFinished(event -> {
            sp1Text.setText("1/1");
        });

        moveRightToSP2.setOnFinished(event -> {
            sp1Text.setText("0/1"); // Reset SP1
            sp2Text.setText("1/1");
        });

        moveLeftToSP3.setOnFinished(event -> {
            sp2Text.setText("0/1"); // Reset SP2
            sp3Text.setText("1/1");
        });

        moveLeftToSP4.setOnFinished(event -> {
            TranslateTransition moveCarUp = new TranslateTransition(Duration.seconds(2), carView);
            moveCarUp.setByY(-220); // auto ajaa pois

            sp3Text.setText("0/1"); // Reset SP3
            sp4Text.setText("1/1");

            // Hide the customer and move the car
            customerView.setVisible(false); // Hide the customer
            moveCarUp.play(); // Move the car
        });


        // Start the animation sequence
        sequence.play();

        // Set up the scene and stage
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Path Simulation");
        primaryStage.show();
    }
}
