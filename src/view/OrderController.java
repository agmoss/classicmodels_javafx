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

public class OrderController {


    @FXML
    private ListView<OrderDetails> lvDetails;
    @FXML
    private AnchorPane apData;
    @FXML
    private AnchorPane apDetails;

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

        populateOrders(); // Table view

        // Display the order details in the right hand side list view
        tvOrders.getSelectionModel().selectedItemProperty().addListener((observable) -> displayOrderDetails());
    }


    private void displayOrderDetails(){

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

            // Display the order total
            taOrderSummary.setText("Order Total: " +result.toString());

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


}
