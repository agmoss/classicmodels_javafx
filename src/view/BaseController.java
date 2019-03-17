package view;

import accessdata.OrderDetailsDao;
import accessdata.OrdersDao;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Order;
import models.OrderDetails;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class BaseController {

    @FXML
    public OrderController orderController;

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
    void initialize() throws IOException {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'view.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'view.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'view.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'view.fxml'.";
        assert selectionTreeView != null : "fx:id=\"selectionTreeView\" was not injected: check your FXML file 'base.fxml'.";

        createTree(); // Side Navigation

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
        itemOrders.setExpanded(false);
        //create child
        TreeItem<String> itemView = new TreeItem<>("View");
        itemOrders.setExpanded(false);

        //root is the parent of itemChild
        root.getChildren().add(itemOrders);
        itemOrders.getChildren().add(itemAdd);
        itemOrders.getChildren().add(itemView);
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

            // TODO: Load the data table for the selected item (break this file up)
            switch (name){
                case "Orders":

                    break;

                default:

                    //apData.getChildren().setAll(new AnchorPane());
                    break;

            }

        }
    }


}
