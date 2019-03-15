package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class OrdersTable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<?, ?> tcOrderNumber;

    @FXML
    private TableColumn<?, ?> tcOrderDate;

    @FXML
    private TableColumn<?, ?> tcRequiredDate;

    @FXML
    private TableColumn<?, ?> tcShippedDate;

    @FXML
    private TableColumn<?, ?> tcStatus;

    @FXML
    private TableColumn<?, ?> tcComments;

    @FXML
    private TableColumn<?, ?> tcCustomerNumber;

    @FXML
    void initialize() {
        assert tcOrderNumber != null : "fx:id=\"tcOrderNumber\" was not injected: check your FXML file 'OrdersTable.fxml'.";
        assert tcOrderDate != null : "fx:id=\"tcOrderDate\" was not injected: check your FXML file 'OrdersTable.fxml'.";
        assert tcRequiredDate != null : "fx:id=\"tcRequiredDate\" was not injected: check your FXML file 'OrdersTable.fxml'.";
        assert tcShippedDate != null : "fx:id=\"tcShippedDate\" was not injected: check your FXML file 'OrdersTable.fxml'.";
        assert tcStatus != null : "fx:id=\"tcStatus\" was not injected: check your FXML file 'OrdersTable.fxml'.";
        assert tcComments != null : "fx:id=\"tcComments\" was not injected: check your FXML file 'OrdersTable.fxml'.";
        assert tcCustomerNumber != null : "fx:id=\"tcCustomerNumber\" was not injected: check your FXML file 'OrdersTable.fxml'.";

    }
}