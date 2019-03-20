package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController {

    @FXML
    TreeView selectionTreeView;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Font x1;
    @FXML
    private Color x2;
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
    void initialize() throws IOException {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'view.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'view.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'view.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'view.fxml'.";
        assert selectionTreeView != null : "fx:id=\"selectionTreeView\" was not injected: check your FXML file 'base.fxml'.";
        assert miLogout != null : "fx:id=\"miLogout\" was not injected: check your FXML file 'base.fxml'.";

        createTree(); // Side Navigation

    }


    // For login
    public void initSessionID(final LoginManager loginManager, String sessionID) {
        sessionLabel.setText(sessionID);
        miLogout.setOnAction(event -> loginManager.logout());
    }


    // Side Navigation
    public void createTree(String... rootItems) {

        //create root
        TreeItem<String> root = new TreeItem<>("Classic Models");
        root.setExpanded(true);

        //create child
        TreeItem<String> itemOrders = new TreeItem<>("Orders");
        itemOrders.setExpanded(true);


        //order options
        TreeItem<String> itemAdd = new TreeItem<>("Add");
        itemOrders.setExpanded(true);
        //create child
        TreeItem<String> itemView = new TreeItem<>("View");
        itemOrders.setExpanded(true);

        //root is the parent of itemChild
        root.getChildren().add(itemOrders);
        itemOrders.getChildren().add(itemAdd);
        itemOrders.getChildren().add(itemView);
        selectionTreeView.setRoot(root);

        // Add the onclick event handler
        selectionTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
    }


    // Determine the name of the node clicked in the side navigation tree view
    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) selectionTreeView.getSelectionModel().getSelectedItem()).getValue();

            switch (name) {
                case "View":

                    loadContent("Order_OrderData");
                    break;

                case "Add":

                    loadContent("add_order");
                    break;

                default:

                    break;

            }
        }
    }

    private void loadContent(String fxml_file) {

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/view/" + fxml_file + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bpContent.setCenter(root);

    }
}
