package accessdata;

import models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersDao implements Dao<Order> {

    // Members
    private Connection conn;

    // Constructor
    public OrdersDao(Connection conn) {
        this.conn = conn;
    }

    // Methods
    @Override
    public List<Order> getItems(int id) throws SQLException {
        return null;
    }

    @Override
    public Optional<Order> getItem(int id) {
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() throws SQLException {

        List<Order> orders = new ArrayList<>();

        // Perform SQL select statement
        try {

            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery("SELECT * from orders;");
            while (results.next()) {

                orders.add(new Order(results.getInt(1), results.getDate(2)
                        , results.getDate(3), results.getDate(4), results.getString(5),
                        results.getString(6), results.getInt(7)));

            }
        } catch (SQLException e) {
            throw new SQLException("Encountered an error when executing given sql statement", e);
        }

        return orders;

    }

    @Override
    public void insert(Order order) throws SQLException {

        try{
            // the mysql insert statement
            String query = " insert into orders (orderNumber, orderDate, requiredDate, shippedDate, status,comments,customerNumber)"
                    + " values (?, ?, ?, ?, ?,?,?)";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt (1, order.getOrderNumber());
            preparedStmt.setDate (2, (Date) order.getOrderDate()); // TODO: Change date type for these
            preparedStmt.setDate   (3, (Date) order.getRequiredDate());
            preparedStmt.setDate(4, (Date) order.getShippedDate());
            preparedStmt.setString(5,order.getStatus());
            preparedStmt.setString(6,order.getComments());
            preparedStmt.setInt    (7, order.getCustomerNumber());

            // execute the prepared statement
            preparedStmt.execute();

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception inserting an order");
            System.err.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public void update(Order order, String[] params) throws SQLException {

        try{
            int nRowsUpdated = 0;

            // the mysql insert statement
            String query = " UPDATE orders SET comments = ?, orderDate = ?, requiredDate=?, shippedDate=?, status = ?, customerNumber = ? WHERE orderNumber = ?";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setDate (2, (Date) order.getOrderDate()); // TODO: Change date type for these
            preparedStmt.setDate   (3, (Date) order.getRequiredDate());
            preparedStmt.setDate(4, (Date) order.getShippedDate());
            preparedStmt.setString(5,order.getStatus());
            preparedStmt.setString(1,order.getComments());
            preparedStmt.setInt    (6, order.getCustomerNumber());
            preparedStmt.setInt (7, order.getOrderNumber());

            nRowsUpdated += preparedStmt.executeUpdate();
            System.out.println(String.format("Updated %d row(s) of data.", nRowsUpdated));

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception inserting an order");
            System.err.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public void delete(Order order) throws SQLException {

        int nRowsDeleted = 0;

        // Delete the order details
        try{

            // the mysql insert statement
            String query = "DELETE FROM orderdetails WHERE orderNumber = ?";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt (1, order.getOrderNumber());

            nRowsDeleted += preparedStmt.executeUpdate();
            System.out.println(String.format("Deleted %d row(s) of data.", nRowsDeleted));


        } catch (SQLException e) {
            System.err.println("Got an exception deleting order details");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

        try{
            // the mysql insert statement
            String query = "DELETE FROM orders WHERE orderNumber = ?";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt (1, order.getOrderNumber());

            nRowsDeleted += preparedStmt.executeUpdate();
            System.out.println(String.format("Deleted %d row(s) of data.", nRowsDeleted));

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception deleting an order");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }
}
