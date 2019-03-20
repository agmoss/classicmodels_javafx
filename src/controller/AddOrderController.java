package controller;

import accessdata.OrderDetailsDao;
import accessdata.OrdersDao;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Order;
import models.OrderDetails;

import java.math.BigDecimal;

public class AddOrderController {

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
    private Button btnInsert;

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
        assert btnInsert != null : "fx:id=\"btnInsert\" was not injected: check your FXML file 'add_order.fxml'.";

        btnAddDetail.setOnAction(actionEvent -> {

            this.addOrderDetail();
        });

        btnInsert.setOnAction(actionEvent -> {
            this.insert();
        });

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


    private void insert(){

        try{

            // Convert user inputs to their proper type
            int addOrderNumber = Integer.parseInt(tfOrderNumber.getText());
            java.sql.Date addOrderDate = java.sql.Date.valueOf(dpOrderDate.getValue());
            java.sql.Date addRequiredDate = java.sql.Date.valueOf(dpRequiredDate.getValue());
            java.sql.Date addShippedDate = java.sql.Date.valueOf(dpShippedDate.getValue());
            int addCustomerNumber = Integer.parseInt(tfCustomerNumber.getText());

            // Create order object to be inserted
            Order insertOrder = new Order(addOrderNumber,addOrderDate,addRequiredDate,addShippedDate,tfStatus.getText(),tfComments.getText(),addCustomerNumber);

            // Get order details to be inserted to the db
            ObservableList<OrderDetails> insertOrderDetails = lvDetails.getItems();

            // Insert order
            OrdersDao ordersDao = new OrdersDao(connection.Connect.getConnection());
            ordersDao.insert(insertOrder);

            // Insert order details
            OrderDetailsDao  orderDetailsDao = new OrderDetailsDao(connection.Connect.getConnection());
            for (OrderDetails x:insertOrderDetails) {
                orderDetailsDao.insert(x);
            }

        }
        catch (Exception e){
            e.printStackTrace(); //TODO: Display error message
        }

    }
}
