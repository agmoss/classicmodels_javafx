package accessdata;

import models.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public Optional<Order> get(long id) {
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
    public void save(Order order) {

    }

    @Override
    public void update(Order order, String[] params) {

    }

    @Override
    public void delete(Order order) {

    }
}
