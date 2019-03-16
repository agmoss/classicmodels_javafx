package accessdata;

import models.Order;
import models.OrderDetails;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDetailsDao implements Dao {

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

    @Override
    public void save(Object o) {

    }

    @Override
    public void update(Object o, String[] params) {

    }

    @Override
    public void delete(Object o) {

    }
}
