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
    private ImageView carView2;
    private ImageView timeView;
    private ImageView roofView;
    private ImageView poofView;

    private Image defaultCustomerGif;
    private Image runningLeftGif;
    private Image runningRightGif;

    public CustomerPathSimulationView() {
        initialize();
    }

    private void initialize() {
        // Set up the background
        Image backgroundImage = new Image("background3.png");
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

        // Car ImageView
        Image carImage = new Image("carBack.png");
        carView = new ImageView(carImage);
        carView.setFitWidth(60);
        carView.setFitHeight(100);
        carView.setX(300);
        carView.setY(250);

        // Car ImageView
        Image carImage2 = new Image("carLeave.png");
        carView2 = new ImageView(carImage2);
        carView2.setFitWidth(56);
        carView2.setFitHeight(105);
        carView2.setX(300);
        carView2.setY(30);

        // time ImageView
        Image timeImage = new Image("giphy.gif");
        timeView = new ImageView(timeImage);
        timeView.setFitWidth(150);
        timeView.setFitHeight(100);
        timeView.setX(445);
        timeView.setY(280);
        timeView.setVisible(false);

        // Set up the roof
        Image roofImage = new Image("roof.png");
        roofView = new ImageView(roofImage);
        roofView.setFitWidth(163);
        roofView.setFitHeight(122);
        roofView.setX(271);
        roofView.setY(28);

        // Set up the poof
        Image poofImage = new Image("confetti.gif");
        poofView = new ImageView(poofImage);
        poofView.setFitWidth(300);
        poofView.setFitHeight(300);
        poofView.setX(390);
        poofView.setY(40);
        poofView.setVisible(false);

        getChildren().addAll(backgroundView, customerView, carView2, carView, roofView, timeView, poofView);
    }

    public void startAnimation() {
        SequentialTransition sequence = createAnimationSequence();
        sequence.play();
    }

    private SequentialTransition createAnimationSequence() {
        // Create all the TranslateTransition objects as in the original code
        TranslateTransition moveUpToSP1 = new TranslateTransition(Duration.seconds(1), customerView);
        moveUpToSP1.setByY(-100);
        moveUpToSP1.setOnFinished(event -> {
            customerView.setImage(runningRightGif);
            customerView.setFitWidth(100);
            customerView.setFitHeight(80);
        });

        TranslateTransition moveRightToSP1 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP1.setByX(140);
        moveRightToSP1.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif);
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveDownToSP1 = new TranslateTransition(Duration.seconds(1), customerView);
        moveDownToSP1.setByY(5);
        moveDownToSP1.setOnFinished(event -> {
            customerView.setImage(runningLeftGif);
            customerView.setFitWidth(140);
            customerView.setFitHeight(80);
        });

        // ------------------------- 2 ------------------------//

        TranslateTransition moveLeftToSP2 = new TranslateTransition(Duration.seconds(3), customerView);
        moveLeftToSP2.setByX(-300);
        moveLeftToSP2.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif);
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveDownToSP2 = new TranslateTransition(Duration.seconds(1), customerView);
        moveDownToSP2.setByY(25);
        moveDownToSP2.setOnFinished(event -> {
            customerView.setImage(runningRightGif);
            customerView.setFitWidth(100);
            customerView.setFitHeight(80);
        });

        // ------------------------- 3 ------------------------//

        TranslateTransition moveRightToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP3.setByX(180);
        moveRightToSP3.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif);
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveUpToSP3 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP3.setByY(-250);
        moveUpToSP3.setOnFinished(event -> {
            customerView.setVisible(false);
        });

        // ------------------------ Car -----------------------//

        TranslateTransition moveCarUp = new TranslateTransition(Duration.seconds(2), carView);
        moveCarUp.setByY(-220);
        moveCarUp.setOnFinished(event -> {
            timeView.setVisible(true);
        });

        TranslateTransition timing = new TranslateTransition(Duration.seconds(3), timeView);
        timing.setByY(1);
        timing.setOnFinished(event -> {
            timeView.setVisible(false);
        });

        TranslateTransition comeBack = new TranslateTransition(Duration.seconds(2), carView2);
        comeBack.setByY(220);
        comeBack.setOnFinished(event -> {
            customerView.setImage(runningRightGif);
            customerView.setFitWidth(100);
            customerView.setFitHeight(80);
            customerView.setVisible(true);
        });

        // ------------------------- 4 ------------------------//

        TranslateTransition moveRightToSP4 = new TranslateTransition(Duration.seconds(2), customerView);
        moveRightToSP4.setByX(160);
        moveRightToSP4.setOnFinished(event -> {
            customerView.setImage(defaultCustomerGif);
            customerView.setFitWidth(150);
            customerView.setFitHeight(100);
        });

        TranslateTransition moveUpToSP4 = new TranslateTransition(Duration.seconds(2), customerView);
        moveUpToSP4.setByY(-160);
        moveUpToSP4.setOnFinished(event -> {
            customerView.setVisible(false);
            poofView.setVisible(true);
        });

        TranslateTransition poof = new TranslateTransition(Duration.seconds(3), poofView);
        poof.setByY(11);
        poof.setOnFinished(event -> {
            poofView.setVisible(false);
        });


        return new SequentialTransition(
                moveUpToSP1, moveRightToSP1, moveDownToSP1,    // 1
                moveLeftToSP2, moveDownToSP2,                  // 2
                moveRightToSP3, moveUpToSP3,                   // 3
                moveCarUp, timing, comeBack,                   // Car
                moveRightToSP4, moveUpToSP4,                   // 4
                poof
        );
    }
}