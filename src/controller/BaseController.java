package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;

public class BaseController {
    private LoginManager lm;


    @FXML
    private Font x3;
    @FXML
    private Color x4;
    @FXML
    private BorderPane bpContent;
    @FXML
    private Label sessionLabel;
    @FXML
    private MenuItem miLogout;
    @FXML
    private Menu mData;
    @FXML
    private MenuItem miAddOrder;
    @FXML
    private MenuItem miViewOrders;


    @FXML
    public void initialize() throws IOException {
        assert bpContent != null : "fx:id=\"bpContent\" was not injected: check your FXML file 'base.fxml'.";
        assert miLogout != null : "fx:id=\"miLogout\" was not injected: check your FXML file 'base.fxml'.";
        assert mData != null : "fx:id=\"mData\" was not injected: check your FXML file 'base.fxml'.";
        assert miAddOrder != null : "fx:id=\"miAddOrder\" was not injected: check your FXML file 'base.fxml'.";
        assert miViewOrders != null : "fx:id=\"miViewOrders\" was not injected: check your FXML file 'base.fxml'.";
        assert sessionLabel != null : "fx:id=\"sessionLabel\" was not injected: check your FXML file 'base.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'base.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'base.fxml'.";

        miViewOrders.setOnAction(event -> loadView());
        miAddOrder.setOnAction(event -> loadAdd());

    }

    // For login
    public void initSessionID(final LoginManager loginManager, String sessionID) {

        this.lm = loginManager;
        sessionLabel.setText(sessionID);
        miLogout.setOnAction(event -> loginManager.logout());
    }


    protected void loadContent(String fxml_file) {

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/view/" + fxml_file + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bpContent.setCenter(root);

    }

    private void loadView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view_orders.fxml"));

        try {

            bpContent.setCenter(loader.load());

            ViewOrdersController voC = loader.getController();

            voC.initView(bpContent); // Pass object to the login controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAdd() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_order.fxml"));

        try {

            bpContent.setCenter(loader.load());

            AddOrderController aoC = loader.getController();

            aoC.initAdd(this.lm); // Pass object to the login controller

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
