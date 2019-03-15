package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import accessdata.OrdersDao;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Order;

public class OrdersTableController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    void initialize() {
        assert tcOrderNumber != null : "fx:id=\"tcOrderNumber\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcOrderDate != null : "fx:id=\"tcOrderDate\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcRequiredDate != null : "fx:id=\"tcRequiredDate\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcShippedDate != null : "fx:id=\"tcShippedDate\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcStatus != null : "fx:id=\"tcStatus\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcComments != null : "fx:id=\"tcComments\" was not injected: check your FXML file 'OrdersTableController.fxml'.";
        assert tcCustomerNumber != null : "fx:id=\"tcCustomerNumber\" was not injected: check your FXML file 'OrdersTableController.fxml'.";

        populateOrders();

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

}