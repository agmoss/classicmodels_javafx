package accessdata;

import models.OrderDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDetailsDao implements Dao<OrderDetails> {

    // Members
    private Connection conn;

    // Constructor
    public OrderDetailsDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List getItems(int id) throws SQLException {

        List<OrderDetails> detailsList = new ArrayList<>();

        // Perform SQL select statement
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT * from orderdetails WHERE orderNumber = ?");

            statement.setInt(1,id);

            ResultSet results = statement.executeQuery();
            while (results.next()) {

                detailsList.add(new OrderDetails(results.getInt(1), results.getString(2),
                        results.getInt(3), results.getBigDecimal(4), results.getShort(5)));

            }
        } catch (SQLException e) {
            throw new SQLException("Encountered an error when executing given sql statement", e);
        }

        return detailsList;
    }

    @Override
    public Optional getItem(int id) throws SQLException {

        return Optional.empty();

    }

    @Override
    public List getAll() throws SQLException {
        return null;
    }



    // THis signature is all messed up
    @Override
    public void insert(OrderDetails orderdetails) throws SQLException {

        try{
            int nRowsInserted = 0;

            // the mysql insert statement
            String query = " insert into orderdetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber)"
                    + " values (?, ?, ?, ?, ?)";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt (1, orderdetails.getOrderNumber());
            preparedStmt.setString (2, orderdetails.getProductCode());
            preparedStmt.setInt  (3, orderdetails.getQuantityOrdered());
            preparedStmt.setBigDecimal(4, orderdetails.getPriceEach());
            preparedStmt.setShort(5,orderdetails.getOrderLineNumber());

            // execute the prepared statement

            nRowsInserted += preparedStmt.executeUpdate();
            System.out.println(String.format("Updated %d row(s) of data.", nRowsInserted));

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception inserting an order");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void update(OrderDetails o, String[] params) throws SQLException {

        try{
            int nRowsUpdated = 0;

            // the mysql insert statement
            String query = " UPDATE orderdetails SET productCode=?, quantityOrdered = ?, priceEach=?, orderLineNumber=? WHERE orderNumber = ?";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt (5, o.getOrderNumber());
            preparedStmt.setString (1, o.getProductCode());
            preparedStmt.setInt  (2, o.getQuantityOrdered());
            preparedStmt.setBigDecimal(3, o.getPriceEach());
            preparedStmt.setShort(4,o.getOrderLineNumber());

            nRowsUpdated += preparedStmt.executeUpdate();
            System.out.println(String.format("Updated %d row(s) of data.", nRowsUpdated));

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception inserting an order");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void delete(OrderDetails o) throws SQLException {

        try{
            int nRowsDeleted = 0;

            // the mysql insert statement
            String query = "DELETE FROM orderdetails WHERE orderNumber = ?";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt (1, o.getOrderNumber());

            nRowsDeleted += preparedStmt.executeUpdate();
            System.out.println(String.format("Deleted %d row(s) of data.", nRowsDeleted));

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception inserting an order");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }
}
