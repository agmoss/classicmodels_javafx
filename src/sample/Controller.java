package sample;

import accessdata.OrdersDao;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    private ListView<?> lvOrders;

    @FXML
    void initialize() {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'sample.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'sample.fxml'.";
        assert tvOrders != null : "fx:id=\"tvOrders\" was not injected: check your FXML file 'sample.fxml'.";
        assert tcOrderNumber != null : "fx:id=\"tcOrderNumber\" was not injected: check your FXML file 'sample.fxml'.";
        assert tcOrderDate != null : "fx:id=\"tcOrderDate\" was not injected: check your FXML file 'sample.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'sample.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'sample.fxml'.";

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

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
