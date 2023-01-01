package fxHt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import turnaus.Turnaus;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Sara
 * @version 3.2.2022
 *
 */
public class HtMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("HtGUIView.fxml"));
            Scene scene = new Scene((BorderPane)ldr.load());
            final HtGUIController ctrl = (HtGUIController)ldr.getController();
            scene.getStylesheets().add(getClass().getResource("ht.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ht");
            primaryStage.show();
            Turnaus turnaus = new Turnaus();
            ctrl.setTurnaus(turnaus);
            ctrl.lueTiedosto("miekkailijat");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}