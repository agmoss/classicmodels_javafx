package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
                case "View":

                    loadContent("Order_OrderData");
                    break;

                case "Add":

                    bpContent.setCenter(null);
                    break;

                default:

                    break;

            }
        }
    }

    private void loadContent(String ui){

        Parent root = null;

        try {
           root =  FXMLLoader.load(getClass().getResource(ui+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bpContent.setCenter(root);

    }

}
