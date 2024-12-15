package simu.controller;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import simu.view.ShrekPathView;

/**
 * Controls and manages the animation sequence for the simulation view.
 */
public class AnimationController {
    private ShrekPathView view;
    private SequentialTransition animation;
    private boolean isPlaying = false;

    /**
     * Initializes the controller with the given view and sets up the animation sequence.
     *
     * @param view The simulation view to manage.
     */
    public AnimationController(ShrekPathView view) {
        this.view = view;
        createNewAnimation();
    }

    /**
     * <STRONG>Utility method</STRONG> - Creates a new animation sequence by clearing any existing animation,
     * resetting the view, and setting up the new animation with proper configuration.
     * Ensures that the animation is ready for playback.
     */
    public void createNewAnimation() {
        if (animation != null) {
            animation.stop();
            animation.getChildren().clear();
        }
        this.animation = view.createAnimationSequence();
        setupAnimation();
    }

    /**
     * Configures the animation sequence with specific properties such as
     * an indefinite cycle count and a handler for when the animation finishes,
     * ensuring continuous playback when active.
     */
    private void setupAnimation() {
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setOnFinished(event -> {
            if (isPlaying) {
                Platform.runLater(this::restartAnimation);
            }
        });
    }

    /**
     * Starts the animation sequence if it is not already playing.
     * Sets the playing flag to true and initiates playback.
     */
    public void startAnimation() {
        if (!isPlaying) {
            isPlaying = true;
            animation.play();
        }
    }

    /**
     * Stops the animation sequence and sets the playing flag to false,
     * effectively pausing the animation.
     */
    public void stopAnimation() {
        isPlaying = false;
        animation.stop();
    }

    /**
     * Restarts the animation by stopping the current animation, resetting
     * the view to its initial state, creating a new animation sequence,
     * and starting the animation again.
     */
    public void restartAnimation() {
        stopAnimation();
        view.resetToInitialState();
        createNewAnimation();
        startAnimation();
    }
}

