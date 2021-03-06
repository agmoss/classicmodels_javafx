package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) { launch(args); }
    @Override public void start(Stage stage) {
        Scene scene = new Scene(new AnchorPane(),1200, 800);

        LoginManager loginManager = new LoginManager(scene);
        loginManager.displayLoginScreen();

        stage.setScene(scene);
        stage.show();
    }
}