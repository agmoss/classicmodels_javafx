package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

//Manages control flow for login
public class LoginManager {
    private Scene scene;

    public LoginManager(Scene scene) {
        this.scene = scene;
    }

     //Callback on authentication
     //Load main app
    public void authenticated(String sessionID) {
        displayBaseView(sessionID);
    }

    //Callback for logout buttonclick
    //Will show the login application screen.
    public  void logout() {
        displayLoginScreen();
    }

    public void displayLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            scene.setRoot(loader.load());

            LoginController controller = loader.getController();
            controller.initManager(this); // Pass object to the login controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Actual application
    private void displayBaseView(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/base.fxml"));

            scene.setRoot(loader.load());

            BaseController controller = loader.getController();
            controller.initSessionID(this, sessionID); // Pass into to the main application (Base Controller)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}