package controller;

import accessdata.OrderDetailsDao;
import accessdata.OrdersDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Order;
import models.OrderDetails;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateOrderController {

    @FXML
    private TextField tfOrderNumber;

    @FXML
    private TextField tfStatus;

    @FXML
    private TextField tfComments;

    @FXML
    private TextField tfCustomerNumber;

    @FXML
    private DatePicker dpOrderDate;

    @FXML
    private DatePicker dpRequiredDate;

    @FXML
    private DatePicker dpShippedDate;

    @FXML
    private TextField tfProductCode;

    @FXML
    private TextField tfQuantityOrdered;

    @FXML
    private TextField tfPriceEach;

    @FXML
    private TextField tfOrderLineNumber;

    @FXML
    private Button btnAddDetail;

    @FXML
    private ListView<OrderDetails> lvDetails;

    @FXML
    private ListView<Order> lvOrders;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnRemoveDetail;

    @FXML
    void initialize() {
        assert tfOrderNumber != null : "fx:id=\"tfOrderNumber\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfStatus != null : "fx:id=\"tfStatus\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfComments != null : "fx:id=\"tfComments\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfCustomerNumber != null : "fx:id=\"tfCustomerNumber\" was not injected: check your FXML file 'add_order.fxml'.";
        assert dpOrderDate != null : "fx:id=\"dpOrderDate\" was not injected: check your FXML file 'add_order.fxml'.";
        assert dpRequiredDate != null : "fx:id=\"dpRequiredDate\" was not injected: check your FXML file 'add_order.fxml'.";
        assert dpShippedDate != null : "fx:id=\"dpShippedDate\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfProductCode != null : "fx:id=\"tfProductCode\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfQuantityOrdered != null : "fx:id=\"tfQuantityOrdered\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfPriceEach != null : "fx:id=\"tfPriceEach\" was not injected: check your FXML file 'add_order.fxml'.";
        assert tfOrderLineNumber != null : "fx:id=\"tfOrderLineNumber\" was not injected: check your FXML file 'add_order.fxml'.";
        assert btnAddDetail != null : "fx:id=\"btnAddDetail\" was not injected: check your FXML file 'add_order.fxml'.";
        assert lvDetails != null : "fx:id=\"lvDetails\" was not injected: check your FXML file 'add_order.fxml'.";
        assert btnUpdate != null : "fx:id=\"btnInsert\" was not injected: check your FXML file 'add_order.fxml'.";
        assert lvOrders != null : "fx:id=\"lvOrders\" was not injected: check your FXML file 'update_order.fxml'.";
        assert btnRemoveDetail != null : "fx:id=\"btnRemoveDetail\" was not injected: check your FXML file 'update_order.fxml'.";


        populateOrders();

        btnAddDetail.setOnAction(actionEvent -> {

            this.addOrderDetail();
        });

        btnUpdate.setOnAction(actionEvent -> {
            this.update();
        });

        btnRemoveDetail.setOnAction(actionEvent -> {
            lvDetails.getItems().remove(lvDetails.getSelectionModel().getSelectedItem());
        });

        // Display the order details in the right hand side list view
        lvOrders.getSelectionModel().selectedItemProperty().addListener((observable) -> populateOrder());

    }

    private void populateOrder() {

        Order selectedOrder = lvOrders.getSelectionModel().getSelectedItem();

        tfOrderNumber.setText(selectedOrder.getOrderNumber()+"");
        tfComments.setText(selectedOrder.getComments());
        tfCustomerNumber.setText(selectedOrder.getCustomerNumber()+"");
        tfStatus.setText(selectedOrder.getStatus());

        java.sql.Date orderDate = (java.sql.Date) selectedOrder.getOrderDate();
        java.sql.Date requiredDate = (java.sql.Date) selectedOrder.getRequiredDate();
        java.sql.Date shippedDate = (java.sql.Date) selectedOrder.getShippedDate();

        dpOrderDate.setValue(orderDate.toLocalDate());
        dpRequiredDate.setValue(requiredDate.toLocalDate());
        dpShippedDate.setValue(shippedDate.toLocalDate());

        //Display the details
        displayOrderDetails(selectedOrder);

    }

    private void addOrderDetail(){

        try{
            // Convert text fields to their proper data types
            int addOrderNumber = Integer.parseInt(tfOrderNumber.getText());
            int addQuantityOrdered = Integer.parseInt(tfQuantityOrdered.getText());
            BigDecimal addPriceEach = new BigDecimal(tfPriceEach.getText());
            Short addOrderLineNumber =  new Short(tfOrderLineNumber.getText());

            // Add an OrderDetails object to the listview
            lvDetails.getItems().add(new OrderDetails(addOrderNumber,tfProductCode.getText(),addQuantityOrdered,addPriceEach,addOrderLineNumber));


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void displayOrderDetails(Order selectedOrder) {

        try {
            // Get the order details
            //Order selectedOrder = lvOrders.getSelectionModel().getSelectedItem();
            Connection conn = connection.Connect.getConnection();
            OrderDetailsDao detailsAccessor = new OrderDetailsDao(conn);
            List<OrderDetails> odList = detailsAccessor.getItems(selectedOrder.getOrderNumber());
            List<OrderDetails> arOdList = new ArrayList<>(odList);

            ObservableList<OrderDetails> displayList = FXCollections.observableArrayList(odList);

            //populate

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

            // Display the orders in the listview
            lvOrders.setItems(orderDataOl);
            lvOrders.setCellFactory(param -> new ListCell<Order>() {
                @Override
                protected void updateItem(Order item, boolean empty) {
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

    private void update(){

        try{

            // Convert user inputs to their proper type
            int updateOrderNumber = Integer.parseInt(tfOrderNumber.getText());
            java.sql.Date updateOrderDate = java.sql.Date.valueOf(dpOrderDate.getValue());
            java.sql.Date updateRequiredDate = java.sql.Date.valueOf(dpRequiredDate.getValue());
            java.sql.Date updateShippedDate = java.sql.Date.valueOf(dpShippedDate.getValue());
            int updateCustomerNumber = Integer.parseInt(tfCustomerNumber.getText());

            // Create order object to be updated
            Order updateOrder = new Order(updateOrderNumber,updateOrderDate,updateRequiredDate,updateShippedDate,tfStatus.getText(),tfComments.getText(),updateCustomerNumber);

            System.out.println(updateOrder.toString());

            // Get order details to be updated to the db
            ObservableList<OrderDetails> updateOrderDetails = lvDetails.getItems();

            // Insert order
            OrdersDao ordersDao = new OrdersDao(connection.Connect.getConnection());
            String[] testStringArray = new String[]{"a", "b", "c"};
            ordersDao.update(updateOrder,testStringArray);


            // Remove previous order details
            OrderDetailsDao  orderDetailsDaoDelete = new OrderDetailsDao(connection.Connect.getConnection());
            orderDetailsDaoDelete.delete(updateOrderDetails.get(0)); //todo: will throw error if this array is empty!

            // Insert order details
            OrderDetailsDao  orderDetailsDaoInsert = new OrderDetailsDao(connection.Connect.getConnection());
            for (OrderDetails x:updateOrderDetails) {
                orderDetailsDaoInsert.insert(x);
            }

        }
        catch (Exception e){
            e.printStackTrace(); //TODO: Display error message
        }

    }

}