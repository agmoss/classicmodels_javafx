package view;

import accessdata.OrderDetailsDao;
import accessdata.OrdersDao;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;
import models.Order;
import models.OrderDetails;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class BaseController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private ListView<OrderDetails> lvDetails;

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


    // Orders Table
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
    private TableColumn<Order, Integer> tcCustomerNumber;

    @FXML
    private TextArea taComments;

    @FXML
    private TextArea taOrderSummary;

    @FXML
    void initialize() throws IOException {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'view.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'view.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'view.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'view.fxml'.";
        assert selectionTreeView != null : "fx:id=\"selectionTreeView\" was not injected: check your FXML file 'base.fxml'.";
        assert apData != null : "fx:id=\"apData\" was not injected: check your FXML file 'base.fxml'.";
        assert apDetails != null : "fx:id=\"apDetails\" was not injected: check your FXML file 'base.fxml'.";
        assert lvDetails != null : "fx:id=\"lvDetails\" was not injected: check your FXML file 'base.fxml'.";
        assert tcOrderNumber != null : "fx:id=\"tcOrderNumber\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcOrderDate != null : "fx:id=\"tcOrderDate\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcRequiredDate != null : "fx:id=\"tcRequiredDate\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcShippedDate != null : "fx:id=\"tcShippedDate\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcStatus != null : "fx:id=\"tcStatus\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcCustomerNumber != null : "fx:id=\"tcCustomerNumber\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert taOrderSummary != null : "fx:id=\"taOrderSummary\" was not injected: check your FXML file 'base.fxml'.";

        createTree(); // Side Navigation
        populateOrders(); // Table view

        // Display the order details in the right hand side list view
        tvOrders.getSelectionModel().selectedItemProperty().addListener((observable) -> {

            try {

                // Get the order details
                Order selectedOrder = tvOrders.getSelectionModel().getSelectedItem();
                Connection conn = connection.Connect.getConnection();
                OrderDetailsDao detailsAccessor = new OrderDetailsDao(conn);
                List<OrderDetails> odList = detailsAccessor.getItems(selectedOrder.getOrderNumber());
                List<OrderDetails> arOdList = new ArrayList<>(odList);

                ObservableList<OrderDetails> displayList =  FXCollections.observableArrayList(odList);

                //populate
                Function<OrderDetails, BigDecimal> totalMapper = a -> a.getPriceEach().multiply(BigDecimal.valueOf(a.getQuantityOrdered()));
                BigDecimal result = arOdList.stream()
                        .map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);


                taOrderSummary.setText(result.toString());


                // Display the order comments
                taComments.setText(selectedOrder.getComments());

                // Clear the listview
                lvDetails.getItems().clear();

                // Display the order details in the listview
                lvDetails.setItems(displayList);
                lvDetails.setCellFactory(param -> new ListCell<OrderDetails>() {
                    @Override
                    protected void updateItem(OrderDetails item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                        }
                    }
                });

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }
    

    private void populateOrders() {

        // Get database connection object
        try {
            Connection conn = connection.Connect.getConnection();

            // Get list of orders from database
            OrdersDao ordersAccessor = new OrdersDao(conn);
            List<Order> orderData = ordersAccessor.getAll();
            ObservableList<Order> orderDataOl = FXCollections.observableArrayList(orderData);

            tvOrders.setItems(orderDataOl);
            tcOrderNumber.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getOrderNumber()));
            tcOrderDate.setCellValueFactory(cell -> new SimpleObjectProperty<Date>(cell.getValue().getOrderDate()));
            tcRequiredDate.setCellValueFactory(cell -> new SimpleObjectProperty<Date>(cell.getValue().getRequiredDate()));
            tcShippedDate.setCellValueFactory(cell -> new SimpleObjectProperty<Date>(cell.getValue().getShippedDate()));
            tcStatus.setCellValueFactory(cell -> new SimpleObjectProperty<String>(cell.getValue().getStatus()));
            tcCustomerNumber.setCellValueFactory(cell -> new SimpleObjectProperty<Integer>(cell.getValue().getCustomerNumber()));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Side Navigation
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

            // TODO: Load the data table for the selected item( break this file up)
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
