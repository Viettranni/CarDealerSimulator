package simu.view;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ShrekPathView extends Pane {

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

    /**
     * Initializes the customer path simulation view by setting up the background, customer,
     * cars, and various other visual elements, and adding them to the scene.
     */
    public ShrekPathView() {
        initialize();
    }

    /**
     * Sets up the initial state of the view, including background, customer, car images,
     * time view, roof view, and poof view. Also adds these elements to the scene.
     */
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

        // Car ImageView 2
        Image carImage2 = new Image("carLeave.png");
        carView2 = new ImageView(carImage2);
        carView2.setFitWidth(56);
        carView2.setFitHeight(105);
        carView2.setX(300);
        carView2.setY(30);

        // Time ImageView
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

    /**
     * Creates a sequence of animations that simulate a customer's journey through various service points,
     * interactions with cars, and visual effects such as poof and time indicators.
     *
     * @return A sequential animation sequence for the customer path.
     */
    public SequentialTransition createAnimationSequence() {
        // Reset all elements to their initial positions
        resetToInitialState();

        // Create all the TranslateTransition objects
        TranslateTransition moveUpToSP1 = createTranslateTransition(customerView, 0, -100, 1);
        moveUpToSP1.setOnFinished(event -> updateCustomerImage(runningRightGif, 100, 80));

        TranslateTransition moveRightToSP1 = createTranslateTransition(customerView, 140, 0, 2);
        moveRightToSP1.setOnFinished(event -> updateCustomerImage(defaultCustomerGif, 150, 100));

        TranslateTransition moveDownToSP1 = createTranslateTransition(customerView, 0, 5, 1);
        moveDownToSP1.setOnFinished(event -> updateCustomerImage(runningLeftGif, 140, 80));

        TranslateTransition moveLeftToSP2 = createTranslateTransition(customerView, -300, 0, 3);
        moveLeftToSP2.setOnFinished(event -> updateCustomerImage(defaultCustomerGif, 150, 100));

        TranslateTransition moveDownToSP2 = createTranslateTransition(customerView, 0, 25, 1);
        moveDownToSP2.setOnFinished(event -> updateCustomerImage(runningRightGif, 100, 80));

        TranslateTransition moveRightToSP3 = createTranslateTransition(customerView, 180, 0, 2);
        moveRightToSP3.setOnFinished(event -> updateCustomerImage(defaultCustomerGif, 150, 100));

        TranslateTransition moveUpToSP3 = createTranslateTransition(customerView, 0, -250, 2);
        moveUpToSP3.setOnFinished(event -> customerView.setVisible(false));

        TranslateTransition moveCarUp = createTranslateTransition(carView, 0, -220, 2);
        moveCarUp.setOnFinished(event -> timeView.setVisible(true));

        TranslateTransition timing = createTranslateTransition(timeView, 0, 1, 3);
        timing.setOnFinished(event -> timeView.setVisible(false));

        TranslateTransition comeBack = createTranslateTransition(carView2, 0, 220, 2);
        comeBack.setOnFinished(event -> {
            updateCustomerImage(runningRightGif, 100, 80);
            customerView.setVisible(true);
        });

        TranslateTransition moveRightToSP4 = createTranslateTransition(customerView, 160, 0, 2);
        moveRightToSP4.setOnFinished(event -> updateCustomerImage(defaultCustomerGif, 150, 100));

        TranslateTransition moveUpToSP4 = createTranslateTransition(customerView, 0, -160, 2);
        moveUpToSP4.setOnFinished(event -> {
            customerView.setVisible(false);
            poofView.setVisible(true);
        });

        TranslateTransition poof = createTranslateTransition(poofView, 0, 11, 3);
        poof.setOnFinished(event -> {
            poofView.setVisible(false);
            customerView.setVisible(true);
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

    /**
     * <STRONG>Utility method</STRONG> - Creates an animation movement for a given node.
     * Uses specified movement in the X and Y directions and duration in seconds.
     *
     * @param node           The ImageView to be animated.
     * @param byX            Distance to move along the X-axis.
     * @param byY            Distance to move along the Y-axis.
     * @param durationSeconds Duration of the animation in seconds.
     * @return A TranslateTransition animation object.
     */
    private TranslateTransition createTranslateTransition(ImageView node, double byX, double byY, double durationSeconds) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(durationSeconds), node);
        transition.setByX(byX);
        transition.setByY(byY);
        return transition;
    }

    /**
     * Updates the customer's image, width, and height during an animation sequence.
     *
     * @param image  The new image to set for the customer.
     * @param width  The new width of the customer image.
     * @param height The new height of the customer image.
     */
    private void updateCustomerImage(Image image, double width, double height) {
        customerView.setImage(image);
        customerView.setFitWidth(width);
        customerView.setFitHeight(height);
    }


    /**
     * Resets all visual elements, such as the customer, cars, time view, and poof view,
     * to their initial states, including position, visibility, and image properties.
     */
    public void resetToInitialState() {
        // Reset customer
        customerView.setImage(defaultCustomerGif);
        customerView.setFitWidth(150);
        customerView.setFitHeight(100);
        customerView.setX((705 - customerView.getFitWidth()) / 2);
        customerView.setY(673 - customerView.getFitHeight() - 10);
        customerView.setVisible(true);
        customerView.setTranslateX(0);
        customerView.setTranslateY(0);

        // Reset cars
        carView.setX(300);
        carView.setY(250);
        carView.setTranslateY(0);

        carView2.setX(300);
        carView2.setY(30);
        carView2.setTranslateY(0);

        // Reset time view
        timeView.setVisible(false);
        timeView.setTranslateY(0);

        // Reset poof view
        poofView.setVisible(false);
        poofView.setTranslateY(0);
    }
}

