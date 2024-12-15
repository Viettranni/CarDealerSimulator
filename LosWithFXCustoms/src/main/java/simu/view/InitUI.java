package simu.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Initializes and launches the graphical user interface (GUI) for the simulation application.
 * Uses JavaFX and FXML to define the layout and styling of the main window.
 */
public class InitUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/simu_view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        //scene.getStylesheets().add("style.css");

        stage.setScene(scene);

        stage.setTitle("Los Santos Customs");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}