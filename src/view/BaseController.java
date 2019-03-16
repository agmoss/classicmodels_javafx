package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

import java.util.*;

public class BaseController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane apData;

    @FXML
    private AnchorPane apDetails;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    TreeView selectionTreeView;

    @FXML
    void initialize() throws IOException {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'view.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'view.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'view.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'view.fxml'.";
        assert selectionTreeView != null : "fx:id=\"selectionTreeView\" was not injected: check your FXML file 'base.fxml'.";
        assert apData != null : "fx:id=\"apData\" was not injected: check your FXML file 'base.fxml'.";
        assert apDetails != null : "fx:id=\"apDetails\" was not injected: check your FXML file 'base.fxml'.";

        createTree();

    }


    public void createTree(String... rootItems) {

        //create root
        TreeItem<String> root = new TreeItem<>("Classic Models");
        root.setExpanded(true);

        //create child
        TreeItem<String> itemCustomers = new TreeItem<>("Customers");
        itemCustomers.setExpanded(false);


        TreeItem<String> itemOrders = new TreeItem<>("Orders");
        itemOrders.setExpanded(false);

        //root is the parent of itemChild
        root.getChildren().add(itemCustomers);
        root.getChildren().add(itemOrders);
        selectionTreeView.setRoot(root);

        // Add the onclick event handler
        selectionTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED,this::handleMouseClicked);
    }


    // Determine the name of the node clicked in the side navigation tree view
    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem)selectionTreeView.getSelectionModel().getSelectedItem()).getValue();

            // Load the data table for the selected item
            switch (name){
                case "Orders":
                    try {

                        apData.getChildren().setAll(Collections.singleton(FXMLLoader.load(getClass().getResource("OrdersTable.fxml"))));
                        break;
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                default:

                    apData.getChildren().setAll(new AnchorPane());
                    break;

            }

        }
    }

}
