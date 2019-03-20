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
            preparedStmt.execute();

            conn.close();


        } catch (SQLException e) {
            System.err.println("Got an exception inserting an order");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void update(OrderDetails o, String[] params) {

    }

    @Override
    public void delete(OrderDetails o) {

    }
}
