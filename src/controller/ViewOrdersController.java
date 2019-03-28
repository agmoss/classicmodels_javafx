package controller;

import accessdata.OrderDetailsDao;
import accessdata.OrdersDao;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import models.Order;
import models.OrderDetails;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class ViewOrdersController {

    private BorderPane bp;

    @FXML
    private ListView<OrderDetails> lvDetails;

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
    private ContextMenu cmOrder;

    @FXML
    private MenuItem miUpdate;

    @FXML
    private MenuItem miDelete;

    @FXML
    private BorderPane bpOrders;

    @FXML
    void initialize() throws IOException {

        assert bpOrders != null : "fx:id=\"bpOrders\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tvOrders != null : "fx:id=\"tvOrders\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tcOrderNumber != null : "fx:id=\"tcOrderNumber\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tcOrderDate != null : "fx:id=\"tcOrderDate\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tcRequiredDate != null : "fx:id=\"tcRequiredDate\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tcShippedDate != null : "fx:id=\"tcShippedDate\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tcStatus != null : "fx:id=\"tcStatus\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert tcCustomerNumber != null : "fx:id=\"tcCustomerNumber\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert cmOrder != null : "fx:id=\"cmOrder\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert miUpdate != null : "fx:id=\"miUpdate\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert miDelete != null : "fx:id=\"miDelete\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert lvDetails != null : "fx:id=\"lvDetails\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert taOrderSummary != null : "fx:id=\"taOrderSummary\" was not injected: check your FXML file 'view_orders.fxml'.";
        assert taComments != null : "fx:id=\"taComments\" was not injected: check your FXML file 'view_orders.fxml'.";

        populateOrders(); // Table view

        initalizeContextMenu();

        // Display the order details in the right hand side list view
        tvOrders.getSelectionModel().selectedItemProperty().addListener((observable) -> displayOrderDetails());

        miUpdate.setOnAction(event -> loadUpdate(tvOrders.getSelectionModel().getSelectedItem()));

    }

    // For going back to the order details view on the right
    public void initView(final BorderPane bp) {
        this.bp = bp;
    }


    private void displayOrderDetails() {

        try {
            // Get the order details
            Order selectedOrder = tvOrders.getSelectionModel().getSelectedItem();
            Connection conn = connection.Connect.getConnection();
            OrderDetailsDao detailsAccessor = new OrderDetailsDao(conn);
            List<OrderDetails> odList = detailsAccessor.getItems(selectedOrder.getOrderNumber());
            List<OrderDetails> arOdList = new ArrayList<>(odList);

            ObservableList<OrderDetails> displayList = FXCollections.observableArrayList(odList);

            //populate
            Function<OrderDetails, BigDecimal> totalMapper = a -> a.getPriceEach().multiply(BigDecimal.valueOf(a.getQuantityOrdered()));
            BigDecimal result = arOdList.stream()
                    .map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String moneyString = formatter.format(result);
            //System.out.println(moneyString);

            // Display the order total
            taOrderSummary.setText("Order Total: " + moneyString);

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


    public void initalizeContextMenu() {
        miUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Order selectedOrder = tvOrders.getSelectionModel().getSelectedItem();

                // Placeholder
                System.out.println(selectedOrder.toString());

            }
        });
    }


    protected void loadUpdate(Order or) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update_order.fxml"));

        try {
            bpOrders.setRight(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        UpdateOrderController uoC = loader.getController();

        // Pass the main content borderpane to the child for re-population
        uoC.initUpdate(or, this.bp);

    }

}
