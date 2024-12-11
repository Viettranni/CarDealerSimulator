package simu.controller;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;

public class CustomerPathSimulationController {
    private CustomerPathSimulationView view;
    private SequentialTransition animation;
    private boolean isPlaying = false;

    public CustomerPathSimulationController(CustomerPathSimulationView view) {
        this.view = view;
        createNewAnimation();
    }

    private void createNewAnimation() {
        if (animation != null) {
            animation.stop();
            animation.getChildren().clear();
        }
        this.animation = view.createAnimationSequence();
        setupAnimation();
    }

    private void setupAnimation() {
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setOnFinished(event -> {
            if (isPlaying) {
                Platform.runLater(this::restartAnimation);
            }
        });
    }

    public void startAnimation() {
        if (!isPlaying) {
            isPlaying = true;
            animation.play();
        }
    }

    public void stopAnimation() {
        isPlaying = false;
        animation.stop();
    }

    public void restartAnimation() {
        stopAnimation();
        view.resetToInitialState();
        createNewAnimation();
        startAnimation();
    }

    public void dispose() {
        stopAnimation();
        if (animation != null) {
            animation.getChildren().clear();
            animation = null;
        }
        view = null;
    }
}

