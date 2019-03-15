package view;

import accessdata.OrdersDao;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Order;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private TableView<Order> tvOrders;

    @FXML
    private TableColumn<Order, Number> tcOrderNumber;

    @FXML
    private TableColumn<Order, Date> tcOrderDate;

    @FXML
    private TableColumn<Order, Date> tcRequiredDate;

    @FXML
    private TableColumn<Order, Date> tcShippedDate;

    @FXML
    private TableColumn<Order, String> tcStatus;

    @FXML
    private TableColumn<Order, String> tcComments;

    @FXML
    private TableColumn<Order, Integer> tcCustomerNumber;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    private ListView<?> lvOrders;

    @FXML
    TreeView selectionTreeView;

    @FXML
    void initialize() {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'view.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'view.fxml'.";
        assert tvOrders != null : "fx:id=\"tvOrders\" was not injected: check your FXML file 'view.fxml'.";
        assert tcOrderNumber != null : "fx:id=\"tcOrderNumber\" was not injected: check your FXML file 'view.fxml'.";
        assert tcOrderDate != null : "fx:id=\"tcOrderDate\" was not injected: check your FXML file 'view.fxml'.";
        assert tcRequiredDate != null : "fx:id=\"tcRequiredDate\" was not injected: check your FXML file 'view.fxml'.";
        assert tcShippedDate != null : "fx:id=\"tcShippedDate\" was not injected: check your FXML file 'view.fxml'.";
        assert tcStatus != null : "fx:id=\"tcStatus\" was not injected: check your FXML file 'view.fxml'.";
        assert tcComments != null : "fx:id=\"tcComments\" was not injected: check your FXML file 'view.fxml'.";
        assert tcCustomerNumber != null : "fx:id=\"tcCustomerNumber\" was not injected: check your FXML file 'view.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'view.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'view.fxml'.";


        populateOrders();

        createTree();

    }

    private void populateOrders() {

        // Get database connection object
        try {
            Connection conn = connection.Connect.getConnection();

            // Get list of orders from database
            OrdersDao ordersAccessor = new OrdersDao(conn);
            List<Order> orderData = ordersAccessor.getAll();
            ObservableList<Order> orderDataOl = FXCollections.observableArrayList(orderData);


            // Prove it works
            for (int i = 0; i < orderData.size(); i++) {
                Order ord = orderDataOl.get(i);
                System.out.println(ord.getOrderNumber());
            }

            tvOrders.setItems(orderDataOl);
            tcOrderNumber.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getOrderNumber()));
            tcOrderDate.setCellValueFactory(cell -> new SimpleObjectProperty<Date>(cell.getValue().getOrderDate()));
            tcRequiredDate.setCellValueFactory(cell -> new SimpleObjectProperty<Date>(cell.getValue().getRequiredDate()));
            tcShippedDate.setCellValueFactory(cell -> new SimpleObjectProperty<Date>(cell.getValue().getShippedDate()));
            tcStatus.setCellValueFactory(cell -> new SimpleObjectProperty<String>(cell.getValue().getStatus()));
            tcComments.setCellValueFactory(cell -> new SimpleObjectProperty<String>(cell.getValue().getComments()));
            tcCustomerNumber.setCellValueFactory(cell -> new SimpleObjectProperty<Integer>(cell.getValue().getCustomerNumber()));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
            System.out.println("Node click: " + name);
        }
    }

}
